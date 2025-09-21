package com.valven.ecommerce.security;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Component
public class InputSanitizer {

    
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
        "(?i).*('|(\\-\\-)|(;)|(\\|)|(\\*)|(%)|(\\+)|(\\=)|(\\<)|(\\>)|(\\()|(\\))|(\\[)|(\\])|(\\{)|(\\})|(\\^)|(\\$)|(\\?)|(\\!)|(\\&)|(\\#)|(\\@)|(\\~)|(\\`)|(\\,)|(\\.)|(\\/)|(\\\\)|(\\:)|(\\;)|(\\')|(\\\")|(\\x00)|(\\x1a)).*"
    );
    
    
    private static final Pattern XSS_PATTERN = Pattern.compile(
        "(?i).*<script.*>.*</script>.*|<.*javascript:.*>.*|.*on\\w+\\s*=.*"
    );
    
    
    private static final Pattern PATH_TRAVERSAL_PATTERN = Pattern.compile(
        ".*(\\.\\./|\\.\\.\\\\|%2e%2e%2f|%2e%2e%5c).*"
    );
    
    
    private static final Pattern COMMAND_INJECTION_PATTERN = Pattern.compile(
        "(?i).*[;&|`$(){}[\\]<>\"'\\\\].*"
    );

    public String sanitizeString(String input) {
        if (!StringUtils.hasText(input)) {
            return "";
        }
        
        
        String sanitized = input.trim();
        
        
        sanitized = sanitized.replaceAll("\\x00", "");
        
        
        sanitized = sanitized.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
        
        
        if (sanitized.length() > 255) {
            sanitized = sanitized.substring(0, 255);
        }
        
        return sanitized;
    }
    
    public String sanitizeText(String input) {
        if (!StringUtils.hasText(input)) {
            return "";
        }
        
        
        String sanitized = input.trim();
        
        
        sanitized = sanitized.replaceAll("\\x00", "");
        
        
        sanitized = sanitized.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
        
        
        if (sanitized.length() > 1000) {
            sanitized = sanitized.substring(0, 1000);
        }
        
        return sanitized;
    }
    
    public String sanitizeEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return "";
        }
        
        String sanitized = email.trim().toLowerCase();
        
        
        if (!isValidEmail(sanitized)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        
        if (sanitized.length() > 255) {
            sanitized = sanitized.substring(0, 255);
        }
        
        return sanitized;
    }
    
    public String sanitizeUrl(String url) {
        if (!StringUtils.hasText(url)) {
            return "";
        }
        
        String sanitized = url.trim();
        
        
        if (!isValidUrl(sanitized)) {
            throw new IllegalArgumentException("Invalid URL format");
        }
        
        
        if (sanitized.length() > 500) {
            sanitized = sanitized.substring(0, 500);
        }
        
        return sanitized;
    }
    
    public boolean containsSqlInjection(String input) {
        return SQL_INJECTION_PATTERN.matcher(input).matches();
    }
    
    public boolean containsXss(String input) {
        return XSS_PATTERN.matcher(input).matches();
    }
    
    public boolean containsPathTraversal(String input) {
        return PATH_TRAVERSAL_PATTERN.matcher(input).matches();
    }
    
    public boolean containsCommandInjection(String input) {
        return COMMAND_INJECTION_PATTERN.matcher(input).matches();
    }
    
    public boolean isSafeInput(String input) {
        return !containsSqlInjection(input) && 
               !containsXss(input) && 
               !containsPathTraversal(input) && 
               !containsCommandInjection(input);
    }
    
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(emailPattern, email);
    }
    
    private boolean isValidUrl(String url) {
        String urlPattern = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
        return Pattern.matches(urlPattern, url);
    }
    
    public String escapeHtml(String input) {
        if (!StringUtils.hasText(input)) {
            return "";
        }
        
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#x27;")
                   .replace("/", "&#x2F;");
    }
    
    public String escapeSql(String input) {
        if (!StringUtils.hasText(input)) {
            return "";
        }
        
        return input.replace("'", "''")
                   .replace("\\", "\\\\")
                   .replace("\"", "\\\"");
    }
}