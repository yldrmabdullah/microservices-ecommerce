package com.valven.ecommerce.userservice.web;

import com.valven.ecommerce.userservice.dto.*;
import com.valven.ecommerce.userservice.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> signup(@Valid @RequestBody SignupRequest request) {
        try {
            log.info("Signup request received for email: {}", request.getEmail());
            AuthResponse response = userService.signup(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User registered successfully", response));
        } catch (Exception e) {
            log.error("Signup failed for email {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<AuthResponse>error(e.getMessage()));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<AuthResponse>> signin(@Valid @RequestBody SigninRequest request) {
        try {
            log.info("Signin request received for email: {}", request.getEmail());
            AuthResponse response = userService.signin(request);
            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        } catch (Exception e) {
            log.error("Signin failed for email {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<AuthResponse>error(e.getMessage()));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validateToken(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            boolean isValid = userService.validateToken(token);
            return ResponseEntity.ok(ApiResponse.success("Token validation completed", isValid));
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<Boolean>error("Invalid token"));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<UserResponse>> getUserInfo(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            UUID userId = userService.getUserIdFromToken(token);
            UserResponse user = userService.getUserById(userId);
            return ResponseEntity.ok(ApiResponse.success("User information retrieved", user));
        } catch (Exception e) {
            log.error("Failed to get user info: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<UserResponse>error("Failed to retrieve user information"));
        }
    }
}
