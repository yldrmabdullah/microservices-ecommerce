package com.valven.ecommerce.orderservice.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderMetricsService {

    private final MeterRegistry meterRegistry;

    public void recordOrderCreated() {
        Counter.builder("orders.created")
                .description("Number of orders created")
                .register(meterRegistry)
                .increment();
        log.debug("Order created metric recorded");
    }

    public void recordOrderValue(double value) {
        Counter.builder("orders.total.value")
                .description("Total value of orders")
                .register(meterRegistry)
                .increment(value);
        log.debug("Order value metric recorded: {}", value);
    }

    public void recordCartItemAdded() {
        Counter.builder("cart.items.added")
                .description("Number of items added to cart")
                .register(meterRegistry)
                .increment();
        log.debug("Cart item added metric recorded");
    }

    public void recordCartItemRemoved() {
        Counter.builder("cart.items.removed")
                .description("Number of items removed from cart")
                .register(meterRegistry)
                .increment();
        log.debug("Cart item removed metric recorded");
    }

    public void recordError(String errorType) {
        Counter.builder("application.errors")
                .tag("type", errorType)
                .description("Number of application errors")
                .register(meterRegistry)
                .increment();
        log.debug("Error metric recorded: {}", errorType);
    }

    public Timer.Sample startOrderCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public void recordOrderCreationTime(Timer.Sample sample) {
        Timer timer = Timer.builder("orders.creation.time")
                .description("Time taken to create an order")
                .register(meterRegistry);
        sample.stop(timer);
        log.debug("Order creation time metric recorded");
    }
}