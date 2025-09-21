package com.valven.ecommerce.orderservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valven.ecommerce.orderservice.domain.Cart;
import com.valven.ecommerce.orderservice.domain.CartItem;
import com.valven.ecommerce.orderservice.domain.Order;
import com.valven.ecommerce.orderservice.repository.CartRepository;
import com.valven.ecommerce.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class CartOrderControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private String testUserId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        testUserId = UUID.randomUUID().toString();
    }

    @Test
    void addItemToCart_ShouldCreateNewCart_WhenCartDoesNotExist() throws Exception {
        
        CartItem cartItem = new CartItem();
        cartItem.setProductId(1L);
        cartItem.setProductName("Test Product");
        cartItem.setPrice(99.99);
        cartItem.setQuantity(2);

        
        mockMvc.perform(post("/api/carts/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartItem))
                .requestAttr("userId", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userId").value(testUserId))
                .andExpect(jsonPath("$.data.items").isArray())
                .andExpect(jsonPath("$.data.items.length()").value(1));
    }

    @Test
    void addItemToCart_ShouldAddToExistingCart_WhenCartExists() throws Exception {
        
        Cart existingCart = new Cart();
        existingCart.setUserId(UUID.fromString(testUserId));
        cartRepository.save(existingCart);

        CartItem cartItem = new CartItem();
        cartItem.setProductId(1L);
        cartItem.setProductName("Test Product");
        cartItem.setPrice(99.99);
        cartItem.setQuantity(2);

        
        mockMvc.perform(post("/api/carts/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartItem))
                .requestAttr("userId", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userId").value(testUserId));
    }

    @Test
    void addItemToCart_ShouldReturnBadRequest_WhenUserIdMissing() throws Exception {
        
        CartItem cartItem = new CartItem();
        cartItem.setProductId(1L);
        cartItem.setProductName("Test Product");
        cartItem.setPrice(99.99);
        cartItem.setQuantity(2);

        
        mockMvc.perform(post("/api/carts/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartItem)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCart_ShouldReturnCart_WhenCartExists() throws Exception {
        
        Cart cart = new Cart();
        cart.setUserId(UUID.fromString(testUserId));
        CartItem item = new CartItem();
        item.setProductId(1L);
        item.setProductName("Test Product");
        item.setPrice(99.99);
        item.setQuantity(2);
        cart.getItems().add(item);
        cartRepository.save(cart);

        
        mockMvc.perform(get("/api/carts")
                .requestAttr("userId", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(testUserId))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(1));
    }

    @Test
    void getCart_ShouldReturnEmptyCart_WhenCartDoesNotExist() throws Exception {
        
        mockMvc.perform(get("/api/carts")
                .requestAttr("userId", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").doesNotExist());
    }

    @Test
    void removeItemFromCart_ShouldRemoveItem_WhenItemExists() throws Exception {
        
        Cart cart = new Cart();
        cart.setUserId(UUID.fromString(testUserId));
        CartItem item = new CartItem();
        item.setProductId(1L);
        item.setProductName("Test Product");
        item.setPrice(99.99);
        item.setQuantity(2);
        cart.getItems().add(item);
        cartRepository.save(cart);

        
        mockMvc.perform(delete("/api/carts/items/1")
                .requestAttr("userId", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(0));
    }

    @Test
    void clearCart_ShouldClearAllItems_WhenCartExists() throws Exception {
        
        Cart cart = new Cart();
        cart.setUserId(UUID.fromString(testUserId));
        CartItem item = new CartItem();
        item.setProductId(1L);
        item.setProductName("Test Product");
        item.setPrice(99.99);
        item.setQuantity(2);
        cart.getItems().add(item);
        cartRepository.save(cart);

        
        mockMvc.perform(delete("/api/carts")
                .requestAttr("userId", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(0));
    }

    @Test
    void createOrder_ShouldCreateOrder_WhenValidData() throws Exception {
        
        Order order = new Order();
        order.setUserId(UUID.fromString(testUserId));
        order.setTotalAmount(199.98);
        CartItem item = new CartItem();
        item.setProductId(1L);
        item.setProductName("Test Product");
        item.setPrice(99.99);
        item.setQuantity(2);
        order.getItems().add(item);

        
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order))
                .requestAttr("userId", testUserId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(testUserId))
                .andExpect(jsonPath("$.totalAmount").value(199.98))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(1));
    }

    @Test
    void getAllOrders_ShouldReturnUserOrders_WhenOrdersExist() throws Exception {
        
        Order order = new Order();
        order.setUserId(UUID.fromString(testUserId));
        order.setTotalAmount(199.98);
        orderRepository.save(order);

        
        mockMvc.perform(get("/api/orders")
                .requestAttr("userId", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].userId").value(testUserId));
    }
}