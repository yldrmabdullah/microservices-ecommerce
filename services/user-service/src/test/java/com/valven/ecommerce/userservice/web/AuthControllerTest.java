package com.valven.ecommerce.userservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valven.ecommerce.userservice.dto.SigninRequest;
import com.valven.ecommerce.userservice.dto.SignupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSignupUserSuccessfully() throws Exception {
        // Given
        SignupRequest request = new SignupRequest();
        request.setName("John Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");

        // When & Then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    void shouldSigninUserSuccessfully() throws Exception {
        // Given
        SigninRequest request = new SigninRequest();
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");

        // When & Then
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldReturnBadRequestForInvalidSignupData() throws Exception {
        // Given
        SignupRequest request = new SignupRequest();
        request.setName(""); // Invalid: empty name
        request.setEmail("invalid-email"); // Invalid: invalid email format
        request.setPassword("123"); // Invalid: too short password

        // When & Then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestForInvalidSigninData() throws Exception {
        // Given
        SigninRequest request = new SigninRequest();
        request.setEmail(""); // Invalid: empty email
        request.setPassword(""); // Invalid: empty password

        // When & Then
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}