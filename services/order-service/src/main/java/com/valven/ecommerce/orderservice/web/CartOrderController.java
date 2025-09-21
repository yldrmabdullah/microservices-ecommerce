package com.valven.ecommerce.orderservice.web;

import com.valven.ecommerce.orderservice.domain.Cart;
import com.valven.ecommerce.orderservice.domain.CartItem;
import com.valven.ecommerce.orderservice.domain.Order;
import com.valven.ecommerce.orderservice.repository.CartRepository;
import com.valven.ecommerce.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class CartOrderController {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    @PostMapping("/carts/items")
    public ResponseEntity<Cart> addItem(@RequestBody CartItem item, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
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
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/carts")
    public ResponseEntity<Cart> getCart(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        return cartRepository.findByUserId(userId).map(ResponseEntity::ok).orElse(ResponseEntity.ok(new Cart()));
    }

    @DeleteMapping("/carts/items/{productId}")
    public ResponseEntity<Cart> removeItem(@PathVariable("productId") Long productId, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if (cart != null) {
            cart.getItems().removeIf(item -> item.getProductId().equals(productId));
            cart = cartRepository.save(cart);
        }
        return ResponseEntity.ok(cart != null ? cart : new Cart());
    }

    @DeleteMapping("/carts")
    public ResponseEntity<Cart> clearCart(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if (cart != null) {
            cart.getItems().clear();
            cart = cartRepository.save(cart);
        }
        return ResponseEntity.ok(cart != null ? cart : new Cart());
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody Order order, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        order.setUserId(userId);
        Order saved = orderRepository.save(order);
        return ResponseEntity.created(URI.create("/api/orders/" + saved.getId())).body(saved);
    }

    @GetMapping("/orders")
    public ResponseEntity<java.util.List<Order>> getAllOrders(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        java.util.List<Order> orders = orderRepository.findByUserId(userId);
        return ResponseEntity.ok(orders);
    }
}


