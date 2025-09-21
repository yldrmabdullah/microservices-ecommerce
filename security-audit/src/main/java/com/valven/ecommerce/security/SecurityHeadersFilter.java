package com.valven.ecommerce.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityHeadersFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        
        httpResponse.setHeader("Content-Security-Policy", 
            "default-src 'self'; " +
            "script-src 'self' 'unsafe-inline' https://unpkg.com; " +
            "style-src 'self' 'unsafe-inline' https://unpkg.com; " +
            "img-src 'self' data: https:; " +
            "font-src 'self' https://unpkg.com; " +
            "connect-src 'self'; " +
            "frame-ancestors 'none'; " +
            "base-uri 'self'; " +
            "form-action 'self'");
        
        
        httpResponse.setHeader("X-Frame-Options", "DENY");
        
        
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        
        
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");
        
        
        httpResponse.setHeader("Strict-Transport-Security", 
            "max-age=31536000; includeSubDomains; preload");
        
        
        httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        
        
        httpResponse.setHeader("Permissions-Policy", 
            "geolocation=(), " +
            "microphone=(), " +
            "camera=(), " +
            "payment=(), " +
            "usb=(), " +
            "magnetometer=(), " +
            "gyroscope=(), " +
            "speaker=()");
        
        
        httpResponse.setHeader("X-Permitted-Cross-Domain-Policies", "none");
        
        
        String requestUri = ((jakarta.servlet.http.HttpServletRequest) request).getRequestURI();
        if (requestUri.contains("/api/auth/") || requestUri.contains("/api/orders/")) {
            httpResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, private");
            httpResponse.setHeader("Pragma", "no-cache");
            httpResponse.setHeader("Expires", "0");
        }
        
        
        httpResponse.setHeader("Server", "");
        
        chain.doFilter(request, response);
    }
}