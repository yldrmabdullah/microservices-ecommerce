package com.valven.ecommerce.aiinsights.model;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InsightResponse {
    private String id;
    private String userId;
    private String insightType;
    private String title;
    private String description;
    private String recommendation;
    private Map<String, Object> metadata;
    private List<String> tags;
    private Double confidence;
    private Instant createdAt;
    private Instant expiresAt;
    private String status;
}