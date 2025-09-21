package com.valven.ecommerce.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SecurityAuditService {

    private static final Logger logger = LoggerFactory.getLogger(SecurityAuditService.class);
    
    private final ConcurrentHashMap<String, AtomicInteger> failedAttempts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> lockoutTimes = new ConcurrentHashMap<>();
    
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCKOUT_DURATION_MINUTES = 5;

    public void logAuthenticationSuccess(String userId, String ipAddress) {
        logger.info("AUTHENTICATION_SUCCESS: User {} from IP {} at {}", 
                   userId, ipAddress, LocalDateTime.now());
        
        
        failedAttempts.remove(userId);
        lockoutTimes.remove(userId);
    }

    public void logAuthenticationFailure(String userId, String ipAddress, String reason) {
        logger.warn("AUTHENTICATION_FAILURE: User {} from IP {} - Reason: {} at {}", 
                   userId, ipAddress, reason, LocalDateTime.now());
        
        
        failedAttempts.computeIfAbsent(userId, k -> new AtomicInteger(0)).incrementAndGet();
        
        
        if (failedAttempts.get(userId).get() >= MAX_FAILED_ATTEMPTS) {
            lockoutTimes.put(userId, LocalDateTime.now());
            logger.error("ACCOUNT_LOCKOUT: User {} locked out due to {} failed attempts", 
                        userId, MAX_FAILED_ATTEMPTS);
        }
    }

    public void logAuthorizationFailure(String userId, String resource, String action) {
        logger.warn("AUTHORIZATION_FAILURE: User {} denied access to {} for action {} at {}", 
                   userId, resource, action, LocalDateTime.now());
    }

    public void logDataAccess(String userId, String resource, String action) {
        logger.info("DATA_ACCESS: User {} accessed {} for action {} at {}", 
                   userId, resource, action, LocalDateTime.now());
    }

    public void logDataModification(String userId, String resource, String action, String details) {
        logger.info("DATA_MODIFICATION: User {} modified {} for action {} - Details: {} at {}", 
                   userId, resource, action, details, LocalDateTime.now());
    }

    public void logSecurityEvent(String eventType, String userId, String details) {
        logger.warn("SECURITY_EVENT: {} - User: {} - Details: {} at {}", 
                   eventType, userId, details, LocalDateTime.now());
    }

    public boolean isAccountLocked(String userId) {
        LocalDateTime lockoutTime = lockoutTimes.get(userId);
        if (lockoutTime == null) {
            return false;
        }
        
        LocalDateTime unlockTime = lockoutTime.plusMinutes(LOCKOUT_DURATION_MINUTES);
        if (LocalDateTime.now().isAfter(unlockTime)) {
            
            lockoutTimes.remove(userId);
            failedAttempts.remove(userId);
            return false;
        }
        
        return true;
    }

    public int getFailedAttempts(String userId) {
        AtomicInteger attempts = failedAttempts.get(userId);
        return attempts != null ? attempts.get() : 0;
    }

    public void resetFailedAttempts(String userId) {
        failedAttempts.remove(userId);
        lockoutTimes.remove(userId);
    }
}