package com.valven.ecommerce.aiinsights.controller;

import com.valven.ecommerce.aiinsights.model.InsightRequest;
import com.valven.ecommerce.aiinsights.model.InsightResponse;
import com.valven.ecommerce.aiinsights.service.AiInsightsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/ai-insights")
@RequiredArgsConstructor
@Tag(name = "AI Insights", description = "AI-powered analytics and recommendations")
public class AiInsightsController {
    
    private final AiInsightsService aiInsightsService;
    
    @PostMapping("/generate")
    @Operation(summary = "Generate AI insight", description = "Generate AI-powered insights and recommendations")
    public ResponseEntity<InsightResponse> generateInsight(@Valid @RequestBody InsightRequest request) {
        log.info("Received insight generation request for user: {}", request.getUserId());
        
        InsightResponse response = aiInsightsService.generateInsight(request);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if AI insights service is healthy")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("AI Insights Service is running");
    }
    
    @GetMapping("/insight-types")
    @Operation(summary = "Get available insight types", description = "Get list of available AI insight types")
    public ResponseEntity<List<String>> getInsightTypes() {
        List<String> insightTypes = List.of(
            "product_recommendation",
            "user_behavior",
            "sales_trend",
            "inventory_optimization",
            "customer_segmentation",
            "price_optimization"
        );
        
        return ResponseEntity.ok(insightTypes);
    }
}