package com.valven.ecommerce.orderservice.web;

import com.valven.ecommerce.orderservice.domain.Cart;
import com.valven.ecommerce.orderservice.domain.CartItem;
import com.valven.ecommerce.orderservice.repository.CartRepository;
import com.valven.ecommerce.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartOrderController.class)
class CartOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    void shouldGetCartSuccessfully() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.setUserId(userId);
        
        when(cartRepository.findByUserId(any(UUID.class))).thenReturn(Optional.of(cart));

        // When & Then
        mockMvc.perform(get("/api/carts")
                        .requestAttr("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestWhenUserIdIsNull() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/carts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddItemToCartSuccessfully() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        CartItem item = new CartItem();
        item.setProductId(1L);
        item.setProductName("Test Product");
        item.setPrice(10.0);
        item.setQuantity(1);
        
        Cart cart = new Cart();
        cart.setUserId(userId);
        
        when(cartRepository.findByUserId(any(UUID.class))).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // When & Then
        mockMvc.perform(post("/api/carts/items")
                        .requestAttr("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1,\"productName\":\"Test Product\",\"price\":10.0,\"quantity\":1}"))
                .andExpect(status().isOk());
    }
}