package com.valven.ecommerce.orderservice.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter orderCreatedCounter(MeterRegistry meterRegistry) {
        return Counter.builder("orders.created")
                .description("Number of orders created")
                .register(meterRegistry);
    }

    @Bean
    public Counter cartItemAddedCounter(MeterRegistry meterRegistry) {
        return Counter.builder("cart.items.added")
                .description("Number of items added to cart")
                .register(meterRegistry);
    }

    @Bean
    public Counter cartItemRemovedCounter(MeterRegistry meterRegistry) {
        return Counter.builder("cart.items.removed")
                .description("Number of items removed from cart")
                .register(meterRegistry);
    }

    @Bean
    public Timer orderCreationTimer(MeterRegistry meterRegistry) {
        return Timer.builder("orders.creation.time")
                .description("Time taken to create an order")
                .register(meterRegistry);
    }

    @Bean
    public Counter orderValueCounter(MeterRegistry meterRegistry) {
        return Counter.builder("orders.total.value")
                .description("Total value of orders")
                .register(meterRegistry);
    }

    @Bean
    public Counter errorCounter(MeterRegistry meterRegistry) {
        return Counter.builder("application.errors")
                .description("Number of application errors")
                .register(meterRegistry);
    }
}