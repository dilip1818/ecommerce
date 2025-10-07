package com.ideas2it.ecommerce.exception;

public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(String message) {
        super(message);
    }
    
    public InvalidOrderException(String orderId, String reason) {
        super(String.format("Invalid order %s: %s", orderId, reason));
    }
}
