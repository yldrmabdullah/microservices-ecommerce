package com.valven.ecommerce.aiinsights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AiInsightsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiInsightsServiceApplication.class, args);
    }

}