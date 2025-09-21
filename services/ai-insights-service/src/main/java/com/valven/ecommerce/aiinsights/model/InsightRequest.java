package com.valven.ecommerce.aiinsights.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InsightRequest {
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotBlank(message = "Insight type is required")
    private String insightType;
    
    @NotNull(message = "Data is required")
    private Object data;
    
    private String context;
    private String preferences;
}