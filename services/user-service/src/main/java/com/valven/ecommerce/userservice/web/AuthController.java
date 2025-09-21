package com.valven.ecommerce.userservice.web;

import com.valven.ecommerce.userservice.dto.*;
import com.valven.ecommerce.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User authentication and registration endpoints")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account and returns authentication token")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> signup(
            @Parameter(description = "User registration details", required = true)
            @Valid @RequestBody SignupRequest request) {
        try {
            log.info("Signup request received for email: {}", request.getEmail());
            AuthResponse response = userService.signup(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(com.valven.ecommerce.userservice.dto.ApiResponse.<AuthResponse>success("User registered successfully", response));
        } catch (Exception e) {
            log.error("Signup failed for email {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(com.valven.ecommerce.userservice.dto.ApiResponse.<AuthResponse>error(e.getMessage()));
        }
    }

    @Operation(summary = "Authenticate user", description = "Authenticates user credentials and returns JWT token")
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<AuthResponse>> signin(
            @Parameter(description = "User login credentials", required = true)
            @Valid @RequestBody SigninRequest request) {
        try {
            log.info("Signin request received for email: {}", request.getEmail());
            AuthResponse response = userService.signin(request);
            return ResponseEntity.ok(com.valven.ecommerce.userservice.dto.ApiResponse.<AuthResponse>success("Login successful", response));
        } catch (Exception e) {
            log.error("Signin failed for email {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(com.valven.ecommerce.userservice.dto.ApiResponse.<AuthResponse>error(e.getMessage()));
        }
    }

    @Operation(summary = "Validate JWT token", description = "Validates the provided JWT token")
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validateToken(
            @Parameter(description = "JWT token in Authorization header", required = true)
            @RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            boolean isValid = userService.validateToken(token);
            return ResponseEntity.ok(com.valven.ecommerce.userservice.dto.ApiResponse.<Boolean>success("Token validation completed", isValid));
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(com.valven.ecommerce.userservice.dto.ApiResponse.<Boolean>error("Invalid token"));
        }
    }

    @Operation(summary = "Get user information", description = "Retrieves user information from JWT token")
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<UserResponse>> getUserInfo(
            @Parameter(description = "JWT token in Authorization header", required = true)
            @RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            UUID userId = userService.getUserIdFromToken(token);
            UserResponse user = userService.getUserById(userId);
            return ResponseEntity.ok(com.valven.ecommerce.userservice.dto.ApiResponse.<UserResponse>success("User information retrieved", user));
        } catch (Exception e) {
            log.error("Failed to get user info: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(com.valven.ecommerce.userservice.dto.ApiResponse.<UserResponse>error("Failed to retrieve user information"));
        }
    }
}
