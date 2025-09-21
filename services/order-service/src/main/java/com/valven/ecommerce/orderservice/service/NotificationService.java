package com.valven.ecommerce.orderservice.service;

import com.valven.ecommerce.orderservice.domain.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    @Async("notificationTaskExecutor")
    public CompletableFuture<Void> sendOrderConfirmationEmail(Order order) {
        try {
            log.info("Sending order confirmation email for order: {}", order.getId());
            
            // Simulate email sending delay
            Thread.sleep(2000);
            
            log.info("Order confirmation email sent successfully for order: {}", order.getId());
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("Failed to send order confirmation email for order: {}", order.getId(), e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async("notificationTaskExecutor")
    public CompletableFuture<Void> sendOrderStatusUpdate(Order order, String status) {
        try {
            log.info("Sending order status update for order: {} with status: {}", order.getId(), status);
            
            // Simulate notification sending delay
            Thread.sleep(1000);
            
            log.info("Order status update sent successfully for order: {}", order.getId());
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("Failed to send order status update for order: {}", order.getId(), e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async("notificationTaskExecutor")
    public CompletableFuture<Void> sendLowStockAlert(String productName, int currentStock) {
        try {
            log.info("Sending low stock alert for product: {} with stock: {}", productName, currentStock);
            
            // Simulate alert sending delay
            Thread.sleep(500);
            
            log.info("Low stock alert sent successfully for product: {}", productName);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("Failed to send low stock alert for product: {}", productName, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async("notificationTaskExecutor")
    public CompletableFuture<Void> sendInventoryUpdate(String productName, int newStock) {
        try {
            log.info("Sending inventory update for product: {} with new stock: {}", productName, newStock);
            
            // Simulate update notification delay
            Thread.sleep(300);
            
            log.info("Inventory update sent successfully for product: {}", productName);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("Failed to send inventory update for product: {}", productName, e);
            return CompletableFuture.failedFuture(e);
        }
    }
}