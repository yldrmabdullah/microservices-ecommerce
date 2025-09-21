package com.valven.ecommerce.userservice.service;

import com.valven.ecommerce.userservice.domain.User;
import com.valven.ecommerce.userservice.dto.AuthResponse;
import com.valven.ecommerce.userservice.dto.SigninRequest;
import com.valven.ecommerce.userservice.dto.SignupRequest;
import com.valven.ecommerce.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private SignupRequest signupRequest;
    private SigninRequest signinRequest;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest();
        signupRequest.setName("John Doe");
        signupRequest.setEmail("john.doe@example.com");
        signupRequest.setPassword("password123");

        signinRequest = new SigninRequest();
        signinRequest.setEmail("john.doe@example.com");
        signinRequest.setPassword("password123");
    }

    @Test
    void shouldSignupUserSuccessfully() {
        // When
        AuthResponse response = userService.signup(signupRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isNotNull();
        assertThat(response.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(response.getName()).isEqualTo("John Doe");

        // Verify user is saved in database
        Optional<User> savedUser = userRepository.findByEmail("john.doe@example.com");
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    void shouldSigninUserSuccessfully() {
        // Given
        userService.signup(signupRequest);

        // When
        AuthResponse response = userService.signin(signinRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isNotNull();
        assertThat(response.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    void shouldThrowExceptionWhenSigningUpWithExistingEmail() {
        // Given
        userService.signup(signupRequest);

        // When & Then
        assertThatThrownBy(() -> userService.signup(signupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User with email john.doe@example.com already exists");
    }

    @Test
    void shouldThrowExceptionWhenSigningInWithInvalidCredentials() {
        // Given
        userService.signup(signupRequest);
        signinRequest.setPassword("wrongpassword");

        // When & Then
        assertThatThrownBy(() -> userService.signin(signinRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid credentials");
    }

    @Test
    void shouldThrowExceptionWhenSigningInWithNonExistentUser() {
        // When & Then
        assertThatThrownBy(() -> userService.signin(signinRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found");
    }
}