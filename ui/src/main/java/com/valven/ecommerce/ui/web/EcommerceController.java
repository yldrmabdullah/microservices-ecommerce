package com.valven.ecommerce.ui.web;

import com.valven.ecommerce.ui.model.Product;
import com.valven.ecommerce.ui.model.CartItem;
import com.valven.ecommerce.ui.model.Cart;
import com.valven.ecommerce.ui.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;

@Controller
public class EcommerceController {

    private static final Logger log = LoggerFactory.getLogger(EcommerceController.class);
    private final WebClient productClient;
    private final WebClient orderClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EcommerceController() {
        this.productClient = WebClient.builder()
                .baseUrl("http://localhost:8080/api")
                .build();
        this.orderClient = WebClient.builder()
                .baseUrl("http://localhost:8080/api")
                .build();
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/auth/login";
        }

        try {
            String response = productClient.get()
                    .uri("/products")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            List<Product> products = parseProductsFromApiResponse(response);
            model.addAttribute("products", products != null ? products : List.of());
            
            model.addAttribute("userName", session.getAttribute("userName"));
            model.addAttribute("userEmail", session.getAttribute("userEmail"));
        } catch (Exception e) {
            log.error("Error loading products: {}", e.getMessage(), e);
            model.addAttribute("products", List.of());
            model.addAttribute("error", "Failed to load products: " + e.getMessage());
        }
        return "index";
    }

    @GetMapping("/products")
    public String products(@RequestParam(required = false) String search,
                          @RequestParam(required = false) String success,
                          @RequestParam(required = false) String error,
                          HttpSession session,
                          Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/auth/login";
        }
        try {
            String uri = search != null ? "/products?q=" + search : "/products";
            String response = productClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            List<Product> products = parseProductsFromApiResponse(response);
            model.addAttribute("products", products != null ? products : List.of());
            model.addAttribute("search", search);
            
            try {
                String cartResponse = orderClient.get()
                        .uri("/carts")
                        .header("Authorization", "Bearer " + session.getAttribute("token"))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
                Cart cart = parseCartFromApiResponse(cartResponse);
                int cartCount = cart != null && cart.getItems() != null ? 
                    cart.getItems().stream().mapToInt(CartItem::getQuantity).sum() : 0;
                model.addAttribute("cartCount", cartCount);
            } catch (Exception e) {
                log.error("Error loading cart count: {}", e.getMessage());
                model.addAttribute("cartCount", 0);
            }
            
            if (success != null) {
                model.addAttribute("success", success);
            }
            if (error != null) {
                model.addAttribute("error", error);
            }
        } catch (Exception e) {
            log.error("Error loading products: {}", e.getMessage(), e);
            model.addAttribute("products", List.of());
            model.addAttribute("error", "Failed to load products: " + e.getMessage());
        }
        return "products";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        try {
            String response = productClient.get()
                    .uri("/products/" + id)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            Product product = parseProductFromApiResponse(response);
            model.addAttribute("product", product);
        } catch (Exception e) {
            log.error("Error loading product: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to load product: " + e.getMessage());
        }
        return "product-detail";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, 
                           @RequestParam String productName,
                           @RequestParam Double price,
                           @RequestParam(defaultValue = "1") Integer quantity,
                           HttpSession session,
                           Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login?error=Please login to add items to cart";
        }
        try {
            String productResponse = productClient.get()
                    .uri("/products/" + productId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            Product product = parseProductFromApiResponse(productResponse);

            if (product == null) {
                log.error("Product not found with id: {}", productId);
                return "redirect:/products?error=Product not found";
            }

            if (!product.hasEnoughStock(quantity)) {
                log.error("Insufficient stock for product {}: available={}, requested={}", 
                         product.getName(), product.getStock(), quantity);
                return "redirect:/products?error=Insufficient stock. Available: " + product.getStock() + ", Requested: " + quantity;
            }

            CartItem item = new CartItem();
            item.setProductId(productId);
            item.setProductName(productName);
            item.setPrice(price);
            item.setQuantity(quantity);

            String cartResponse = orderClient.post()
                    .uri("/carts/items")
                    .header("Authorization", "Bearer " + session.getAttribute("token"))
                    .bodyValue(item)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("Cart response: {}", cartResponse);
            
            try {
                productClient.post()
                        .uri("/products/" + productId + "/stock/reduce?quantity=" + quantity)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
                log.info("Stock reduced for product {} by quantity {}", productId, quantity);
            } catch (Exception e) {
                log.error("Failed to reduce stock for product {}: {}", productId, e.getMessage());
            }
            
            log.info("Product {} added to cart for user {} with quantity {}", productName, userId, quantity);
            
            return "redirect:/products?success=Product " + productName + " added to cart successfully!";
            
        } catch (Exception e) {
            log.error("Error adding product to cart: {}", e.getMessage(), e);
            return "redirect:/products?error=Failed to add product to cart: " + e.getMessage();
        }
    }

    @GetMapping("/cart")
    public String cart(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login?error=Please login to view cart";
        }
        
        try {
            String cartResponse = orderClient.get()
                    .uri("/carts")
                    .header("Authorization", "Bearer " + session.getAttribute("token"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            Cart cart = parseCartFromApiResponse(cartResponse);
            model.addAttribute("cart", cart);
            model.addAttribute("userId", userId);
        } catch (Exception e) {
            log.error("Error loading cart: {}", e.getMessage(), e);
            Cart emptyCart = new Cart();
            emptyCart.setUserId(userId);
            model.addAttribute("cart", emptyCart);
            model.addAttribute("error", "Failed to load cart: " + e.getMessage());
        }
        return "cart-simple";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long productId, 
                                HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login?error=Please login to modify cart";
        }
        
        try {
            String cartResponse = orderClient.get()
                    .uri("/carts")
                    .header("Authorization", "Bearer " + session.getAttribute("token"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            Cart cart = parseCartFromApiResponse(cartResponse);
            int quantityToRestore = 0;
            
            if (cart != null && cart.getItems() != null) {
                for (CartItem item : cart.getItems()) {
                    if (item.getProductId().equals(productId)) {
                        quantityToRestore = item.getQuantity();
                        break;
                    }
                }
            }
            
            orderClient.delete()
                    .uri("/carts/items/" + productId)
                    .header("Authorization", "Bearer " + session.getAttribute("token"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            if (quantityToRestore > 0) {
                try {
                    productClient.post()
                            .uri("/products/" + productId + "/stock/add?quantity=" + quantityToRestore)
                            .retrieve()
                            .bodyToMono(String.class)
                            .block();
                    log.info("Stock restored for product {} by quantity {}", productId, quantityToRestore);
                } catch (Exception e) {
                    log.error("Failed to restore stock for product {}: {}", productId, e.getMessage());
                }
            }
            
            log.info("Product {} removed from cart for user {}", productId, userId);
            return "redirect:/cart?success=Product removed from cart";
            
        } catch (Exception e) {
            log.error("Error removing product from cart: {}", e.getMessage(), e);
            return "redirect:/cart?error=Failed to remove product from cart";
        }
    }

    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login?error=Please login to modify cart";
        }
        
        try {
            String cartResponse = orderClient.get()
                    .uri("/carts")
                    .header("Authorization", "Bearer " + session.getAttribute("token"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            Cart cart = parseCartFromApiResponse(cartResponse);
            
            orderClient.delete()
                    .uri("/carts")
                    .header("Authorization", "Bearer " + session.getAttribute("token"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            if (cart != null && cart.getItems() != null) {
                for (CartItem item : cart.getItems()) {
                    try {
                        productClient.post()
                                .uri("/products/" + item.getProductId() + "/stock/add?quantity=" + item.getQuantity())
                                .retrieve()
                                .bodyToMono(String.class)
                                .block();
                        log.info("Stock restored for product {} by quantity {}", item.getProductId(), item.getQuantity());
                    } catch (Exception e) {
                        log.error("Failed to restore stock for product {}: {}", item.getProductId(), e.getMessage());
                    }
                }
            }
            
            log.info("Cart cleared for user {}", userId);
            return "redirect:/cart?success=All items removed from cart";
            
        } catch (Exception e) {
            log.error("Error clearing cart: {}", e.getMessage(), e);
            return "redirect:/cart?error=Failed to clear cart";
        }
    }

    @PostMapping("/order/create")
    public String createOrder(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login?error=Please login to create order";
        }
        
        try {
            Cart cart = orderClient.get()
                    .uri("/carts")
                    .header("Authorization", "Bearer " + session.getAttribute("token"))
                    .retrieve()
                    .bodyToMono(Cart.class)
                    .block();

            if (cart != null && !cart.getItems().isEmpty()) {
                Order order = new Order();
                order.setUserId(userId);
                order.setItems(cart.getItems());
                order.setTotalAmount(cart.getItems().stream()
                        .mapToDouble(item -> item.getPrice() * item.getQuantity())
                        .sum());

                Order createdOrder = orderClient.post()
                        .uri("/orders")
                        .header("Authorization", "Bearer " + session.getAttribute("token"))
                        .bodyValue(order)
                        .retrieve()
                        .bodyToMono(Order.class)
                        .block();

                model.addAttribute("order", createdOrder);
                return "order-success";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create order: " + e.getMessage());
        }
        return "redirect:/cart";
    }

    @GetMapping("/orders")
    public String orders(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login?error=Please login to view orders";
        }
        
        try {
            List<Order> orders = orderClient.get()
                    .uri("/orders")
                    .header("Authorization", "Bearer " + session.getAttribute("token"))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Order>>() {})
                    .block();
            
            model.addAttribute("orders", orders != null ? orders : List.of());
        } catch (Exception e) {
            log.error("Error loading orders: {}", e.getMessage(), e);
            model.addAttribute("orders", List.of());
            model.addAttribute("error", "Failed to load orders: " + e.getMessage());
        }
        return "orders";
    }
    
    private List<Product> parseProductsFromApiResponse(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode dataNode = rootNode.get("data");
            
            if (dataNode != null && dataNode.isArray()) {
                List<Product> products = new ArrayList<>();
                for (JsonNode productNode : dataNode) {
                    Product product = new Product();
                    product.setId(productNode.get("id").asLong());
                    product.setSku(productNode.has("sku") ? productNode.get("sku").asText() : null);
                    product.setName(productNode.get("name").asText());
                    product.setDescription(productNode.get("description").asText());
                    product.setPrice(productNode.get("price").asDouble());
                    product.setStock(productNode.get("stock").asInt());
                    product.setCategory(productNode.has("category") ? productNode.get("category").asText() : null);
                    product.setImageUrl(productNode.has("imageUrl") ? productNode.get("imageUrl").asText() : null);
                    products.add(product);
                }
                return products;
            }
        } catch (Exception e) {
            log.error("Error parsing products from API response: {}", e.getMessage(), e);
        }
        return new ArrayList<>();
    }
    
    private Product parseProductFromApiResponse(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode dataNode = rootNode.get("data");
            
            if (dataNode != null) {
                Product product = new Product();
                product.setId(dataNode.get("id").asLong());
                product.setSku(dataNode.has("sku") ? dataNode.get("sku").asText() : null);
                product.setName(dataNode.get("name").asText());
                product.setDescription(dataNode.get("description").asText());
                product.setPrice(dataNode.get("price").asDouble());
                product.setStock(dataNode.get("stock").asInt());
                product.setCategory(dataNode.has("category") ? dataNode.get("category").asText() : null);
                product.setImageUrl(dataNode.has("imageUrl") ? dataNode.get("imageUrl").asText() : null);
                return product;
            }
        } catch (Exception e) {
            log.error("Error parsing product from API response: {}", e.getMessage(), e);
        }
        return null;
    }
    
    private Cart parseCartFromApiResponse(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            Cart cart = new Cart();
            cart.setUserId(rootNode.has("userId") ? rootNode.get("userId").asText() : "user123");
            
            if (rootNode.has("items") && rootNode.get("items").isArray()) {
                for (JsonNode itemNode : rootNode.get("items")) {
                    CartItem item = new CartItem();
                    item.setProductId(itemNode.get("productId").asLong());
                    item.setProductName(itemNode.get("productName").asText());
                    item.setPrice(itemNode.get("price").asDouble());
                    item.setQuantity(itemNode.get("quantity").asInt());
                    cart.getItems().add(item);
                }
            }
            return cart;
        } catch (Exception e) {
            log.error("Error parsing cart from API response: {}", e.getMessage(), e);
            return new Cart();
        }
    }
}
