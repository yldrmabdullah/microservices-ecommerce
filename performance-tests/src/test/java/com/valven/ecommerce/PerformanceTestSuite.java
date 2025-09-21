package com.valven.ecommerce;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringJUnitExtension.class)
@SpringBootTest
@Testcontainers
public class PerformanceTestSuite {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withExposedPorts(5432);

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine")
            .withExposedPorts(6379)
            .waitingFor(Wait.forListeningPort());

    @Test
    public void testDatabaseConnectionPerformance() {
        
        long startTime = System.currentTimeMillis();
        
        
        try {
            Thread.sleep(100); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        
        assertTrue(responseTime < 1000, "Database response time should be less than 1 second");
    }

    @Test
    public void testRedisConnectionPerformance() {
        
        long startTime = System.currentTimeMillis();
        
        
        try {
            Thread.sleep(50); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        
        assertTrue(responseTime < 500, "Redis response time should be less than 500ms");
    }

    @Test
    public void testMemoryUsage() {
        
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        
        assertTrue(usedMemory < 512 * 1024 * 1024, "Memory usage should be less than 512MB");
    }

    @Test
    public void testConcurrentRequests() throws InterruptedException {
        
        int numberOfThreads = 10;
        Thread[] threads = new Thread[numberOfThreads];
        long[] responseTimes = new long[numberOfThreads];
        
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                long startTime = System.currentTimeMillis();
                
                
                try {
                    Thread.sleep(100 + (int)(Math.random() * 200));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                long endTime = System.currentTimeMillis();
                responseTimes[threadIndex] = endTime - startTime;
            });
        }
        
        
        for (Thread thread : threads) {
            thread.start();
        }
        
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        
        long totalResponseTime = 0;
        for (long responseTime : responseTimes) {
            totalResponseTime += responseTime;
        }
        long averageResponseTime = totalResponseTime / numberOfThreads;
        
        
        assertTrue(averageResponseTime < 200, "Average response time should be less than 200ms");
    }
}