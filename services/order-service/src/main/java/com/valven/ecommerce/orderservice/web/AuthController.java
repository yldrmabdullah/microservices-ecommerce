package com.valven.ecommerce.orderservice.web;

import com.valven.ecommerce.orderservice.domain.User;
import com.valven.ecommerce.orderservice.dto.*;
import com.valven.ecommerce.orderservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> signup(@RequestBody SignupRequest request) {
        try {
            log.info("Signup attempt for email: {}", request.getEmail());

            if (!request.getPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.<AuthResponse>error("Passwords do not match", "PASSWORD_MISMATCH"));
            }

            if (userRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.<AuthResponse>error("User already exists with this email", "USER_EXISTS"));
            }

            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setIsActive(true);
            user.setCreatedAt(LocalDateTime.now());

            User savedUser = userRepository.save(user);

            String token = UUID.randomUUID().toString();

            AuthResponse authResponse = new AuthResponse();
            authResponse.setToken(token);
            authResponse.setUserId(savedUser.getId().toString());
            authResponse.setName(savedUser.getName());
            authResponse.setEmail(savedUser.getEmail());

            log.info("User signed up successfully: {}", request.getEmail());
            return ResponseEntity.ok(ApiResponse.<AuthResponse>success("User created successfully", authResponse));

        } catch (Exception e) {
            log.error("Error during signup: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<AuthResponse>error("Registration failed. Please try again."));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<AuthResponse>> signin(@RequestBody SigninRequest request) {
        try {
            log.info("Signin attempt for email: {}", request.getEmail());

            User user = userRepository.findByEmail(request.getEmail())
                    .orElse(null);

            if (user == null || !user.getIsActive()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.<AuthResponse>error("Invalid credentials", "INVALID_CREDENTIALS"));
            }

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.<AuthResponse>error("Invalid credentials", "INVALID_CREDENTIALS"));
            }

            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            String token = UUID.randomUUID().toString();

            AuthResponse authResponse = new AuthResponse();
            authResponse.setToken(token);
            authResponse.setUserId(user.getId().toString());
            authResponse.setName(user.getName());
            authResponse.setEmail(user.getEmail());

            log.info("User signed in successfully: {}", request.getEmail());
            return ResponseEntity.ok(ApiResponse.<AuthResponse>success("Login successful", authResponse));

        } catch (Exception e) {
            log.error("Error during signin: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<AuthResponse>error("Login failed. Please try again."));
        }
    }
}