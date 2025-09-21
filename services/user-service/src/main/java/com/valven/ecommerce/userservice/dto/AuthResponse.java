package com.valven.ecommerce.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String type;
    private UUID userId;
    private String name;
    private String email;
    private long expiresIn;

    // Constructor for the specific usage pattern
    public AuthResponse(String token, String type, UUID userId, String name, String email, long expiresIn) {
        this.token = token;
        this.type = type;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.expiresIn = expiresIn;
    }

    // Default constructor with type = "Bearer"
    public AuthResponse(String token, UUID userId, String name, String email, long expiresIn) {
        this.token = token;
        this.type = "Bearer";
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.expiresIn = expiresIn;
    }
}
