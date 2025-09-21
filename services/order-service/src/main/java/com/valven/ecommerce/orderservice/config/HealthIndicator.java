package com.valven.ecommerce.orderservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseHealthIndicator implements HealthIndicator {

    private final JpaRepository<?, ?> orderRepository;
    private final JpaRepository<?, ?> cartRepository;

    @Override
    public Health health() {
        try {
            // Test database connectivity
            long orderCount = orderRepository.count();
            long cartCount = cartRepository.count();
            
            return Health.up()
                    .withDetail("orders", orderCount)
                    .withDetail("carts", cartCount)
                    .withDetail("database", "Connected")
                    .build();
        } catch (Exception e) {
            log.error("Database health check failed", e);
            return Health.down()
                    .withDetail("database", "Disconnected")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}