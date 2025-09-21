package com.valven.ecommerce.userservice.service;

import com.valven.ecommerce.userservice.domain.User;
import com.valven.ecommerce.userservice.dto.AuthResponse;
import com.valven.ecommerce.userservice.dto.SigninRequest;
import com.valven.ecommerce.userservice.dto.SignupRequest;
import com.valven.ecommerce.userservice.repository.UserRepository;
import com.valven.ecommerce.userservice.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private SignupRequest signupRequest;
    private SigninRequest signinRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setIsActive(true);

        signupRequest = new SignupRequest();
        signupRequest.setName("Test User");
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setConfirmPassword("password123");

        signinRequest = new SigninRequest();
        signinRequest.setEmail("test@example.com");
        signinRequest.setPassword("password123");
    }

    @Test
    void signup_ShouldCreateNewUser_WhenValidRequest() {
        
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtUtil.generateToken(any(UUID.class), anyString())).thenReturn("jwt-token");

        
        AuthResponse response = userService.signup(signupRequest);

        
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals(testUser.getId(), response.getUserId());
        assertEquals(testUser.getName(), response.getName());
        assertEquals(testUser.getEmail(), response.getEmail());

        verify(userRepository).existsByEmail("test@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
        verify(jwtUtil).generateToken(any(UUID.class), anyString());
    }

    @Test
    void signup_ShouldThrowException_WhenUserAlreadyExists() {
        
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        
        assertThrows(RuntimeException.class, () -> userService.signup(signupRequest));
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void signin_ShouldReturnToken_WhenValidCredentials() {
        
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(any(UUID.class), anyString())).thenReturn("jwt-token");

        
        AuthResponse response = userService.signin(signinRequest);

        
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals(testUser.getId(), response.getUserId());
        assertEquals(testUser.getName(), response.getName());
        assertEquals(testUser.getEmail(), response.getEmail());

        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtUtil).generateToken(testUser.getId(), testUser.getEmail());
    }

    @Test
    void signin_ShouldThrowException_WhenInvalidCredentials() {
        
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        
        assertThrows(RuntimeException.class, () -> userService.signin(signinRequest));
        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtUtil, never()).generateToken(any(UUID.class), anyString());
    }

    @Test
    void signin_ShouldThrowException_WhenUserNotFound() {
        
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        
        assertThrows(RuntimeException.class, () -> userService.signin(signinRequest));
        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void validateToken_ShouldReturnTrue_WhenValidToken() {
        
        String token = "valid-token";
        when(jwtUtil.validateToken(token)).thenReturn(true);

        
        boolean result = userService.validateToken(token);

        
        assertTrue(result);
        verify(jwtUtil).validateToken(token);
    }

    @Test
    void validateToken_ShouldReturnFalse_WhenInvalidToken() {
        
        String token = "invalid-token";
        when(jwtUtil.validateToken(token)).thenReturn(false);

        
        boolean result = userService.validateToken(token);

        
        assertFalse(result);
        verify(jwtUtil).validateToken(token);
    }
}