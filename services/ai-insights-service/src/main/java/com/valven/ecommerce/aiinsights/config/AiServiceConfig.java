package com.valven.ecommerce.aiinsights.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ai")
public class AiServiceConfig {
    
    private OpenAi openai = new OpenAi();
    private Anthropic anthropic = new Anthropic();
    
    @Data
    public static class OpenAi {
        private String apiKey;
        private String baseUrl;
    }
    
    @Data
    public static class Anthropic {
        private String apiKey;
        private String baseUrl;
    }
}