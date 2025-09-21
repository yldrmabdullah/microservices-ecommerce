package com.valven.ecommerce.orderservice.web;

import com.valven.ecommerce.orderservice.domain.Cart;
import com.valven.ecommerce.orderservice.domain.CartItem;
import com.valven.ecommerce.orderservice.domain.Order;
import com.valven.ecommerce.orderservice.dto.ApiResponse;
import com.valven.ecommerce.orderservice.repository.CartRepository;
import com.valven.ecommerce.orderservice.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartOrderController {

    private static final Logger log = LoggerFactory.getLogger(CartOrderController.class);
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    @PostMapping("/carts/items")
    public ResponseEntity<ApiResponse<Cart>> addItem(@Valid @RequestBody CartItem item, HttpServletRequest request) {
        String userIdStr = (String) request.getAttribute("userId");
        if (userIdStr == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<Cart>error("User ID not found in request", "MISSING_USER_ID"));
        }
        
        try {
            UUID userId = UUID.fromString(userIdStr);
            Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
                Cart c = new Cart();
                c.setUserId(userId);
                return c;
            });
            
            boolean itemExists = false;
            for (CartItem existingItem : cart.getItems()) {
                if (existingItem.getProductId().equals(item.getProductId())) {
                    existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                    itemExists = true;
                    break;
                }
            }
            
            if (!itemExists) {
                cart.getItems().add(item);
            }
            
            Cart saved = cartRepository.save(cart);
            log.info("Item added to cart for user: {}, product: {}, quantity: {}", 
                    userIdStr, item.getProductId(), item.getQuantity());
            return ResponseEntity.ok(ApiResponse.<Cart>success("Item added to cart successfully", saved));
        } catch (Exception e) {
            log.error("Error adding item to cart: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<Cart>error("Failed to add item to cart: " + e.getMessage(), "CART_ADD_ERROR"));
        }
    }

    @GetMapping("/carts")
    public ResponseEntity<Cart> getCart(HttpServletRequest request) {
        String userIdStr = (String) request.getAttribute("userId");
        if (userIdStr == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            UUID userId = UUID.fromString(userIdStr);
            return cartRepository.findByUserId(userId).map(ResponseEntity::ok).orElse(ResponseEntity.ok(new Cart()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/carts/items/{productId}")
    public ResponseEntity<Cart> removeItem(@PathVariable("productId") Long productId, HttpServletRequest request) {
        String userIdStr = (String) request.getAttribute("userId");
        if (userIdStr == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            UUID userId = UUID.fromString(userIdStr);
            Cart cart = cartRepository.findByUserId(userId).orElse(null);
            if (cart != null) {
                cart.getItems().removeIf(item -> item.getProductId().equals(productId));
                cart = cartRepository.save(cart);
            }
            return ResponseEntity.ok(cart != null ? cart : new Cart());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/carts")
    public ResponseEntity<Cart> clearCart(HttpServletRequest request) {
        String userIdStr = (String) request.getAttribute("userId");
        if (userIdStr == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            UUID userId = UUID.fromString(userIdStr);
            Cart cart = cartRepository.findByUserId(userId).orElse(null);
            if (cart != null) {
                cart.getItems().clear();
                cart = cartRepository.save(cart);
            }
            return ResponseEntity.ok(cart != null ? cart : new Cart());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody Order order, HttpServletRequest request) {
        String userIdStr = (String) request.getAttribute("userId");
        if (userIdStr == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            UUID userId = UUID.fromString(userIdStr);
            order.setUserId(userId);
            Order saved = orderRepository.save(order);
            return ResponseEntity.created(URI.create("/api/orders/" + saved.getId())).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<java.util.List<Order>> getAllOrders(HttpServletRequest request) {
        String userIdStr = (String) request.getAttribute("userId");
        if (userIdStr == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            UUID userId = UUID.fromString(userIdStr);
            java.util.List<Order> orders = orderRepository.findByUserId(userId);
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}


