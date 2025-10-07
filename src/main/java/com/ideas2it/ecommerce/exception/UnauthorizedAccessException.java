package com.ideas2it.ecommerce.exception;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
    
    public UnauthorizedAccessException(String resource, String action) {
        super(String.format("Unauthorized to %s %s", action, resource));
    }
}
