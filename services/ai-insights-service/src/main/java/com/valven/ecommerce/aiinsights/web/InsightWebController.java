package com.valven.ecommerce.aiinsights.web;

import com.valven.ecommerce.aiinsights.model.InsightRequest;
import com.valven.ecommerce.aiinsights.model.InsightResponse;
import com.valven.ecommerce.aiinsights.service.AiInsightsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/insights")
@RequiredArgsConstructor
public class InsightWebController {
    
    private final AiInsightsService aiInsightsService;
    
    @GetMapping
    public String insightsPage(@RequestParam(required = false) String type, Model model) {
        log.info("Loading insights page with type: {}", type);
        
        // Set default values for the form
        model.addAttribute("userId", "user123"); // In real app, get from session/security context
        model.addAttribute("insightType", type);
        
        // In a real application, you would fetch existing insights for the user
        // For now, we'll leave it empty
        model.addAttribute("insights", List.of());
        
        return "insights";
    }
    
    @GetMapping("/types")
    public String insightTypesPage() {
        log.info("Loading insight types page");
        return "insight-types";
    }
    
    @GetMapping("/{id}")
    public String insightDetailPage(@PathVariable String id, Model model) {
        log.info("Loading insight detail page for ID: {}", id);
        
        // In a real application, you would fetch the insight by ID
        // For now, we'll create a sample insight
        InsightResponse sampleInsight = createSampleInsight(id);
        model.addAttribute("insight", sampleInsight);
        
        return "insight-detail";
    }
    
    @PostMapping("/generate")
    public String generateInsight(@ModelAttribute InsightRequest request, Model model) {
        log.info("Generating insight for user: {} with type: {}", request.getUserId(), request.getInsightType());
        
        try {
            InsightResponse response = aiInsightsService.generateInsight(request);
            model.addAttribute("success", "Insight generated successfully!");
            model.addAttribute("insight", response);
            
            // Redirect to the detail page
            return "redirect:/insights/" + response.getId();
            
        } catch (Exception e) {
            log.error("Error generating insight: {}", e.getMessage());
            model.addAttribute("error", "Failed to generate insight: " + e.getMessage());
            model.addAttribute("userId", request.getUserId());
            model.addAttribute("insightType", request.getInsightType());
            model.addAttribute("context", request.getContext());
            model.addAttribute("preferences", request.getPreferences());
            model.addAttribute("insights", List.of());
            
            return "insights";
        }
    }
    
    private InsightResponse createSampleInsight(String id) {
        return InsightResponse.builder()
                .id(id)
                .userId("user123")
                .insightType("product_recommendation")
                .title("Personalized Product Recommendations")
                .description("Based on your shopping behavior and preferences, we've identified several product categories that match your interests.")
                .recommendation("We recommend exploring Electronics, Home & Garden, and Sports & Outdoors categories based on your previous purchases and browsing behavior.")
                .metadata(java.util.Map.of(
                    "requestId", "req-" + System.currentTimeMillis(),
                    "timestamp", java.time.Instant.now().toString(),
                    "dataSize", 1024
                ))
                .tags(List.of("recommendation", "personalization", "products"))
                .confidence(0.85)
                .createdAt(java.time.Instant.now())
                .expiresAt(java.time.Instant.now().plusSeconds(7 * 24 * 60 * 60)) // 7 days
                .status("ACTIVE")
                .build();
    }
}