package com.ideas2it.ecommerce.exception;

public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
    
    public PaymentException(String orderId, String reason) {
        super(String.format("Payment failed for order %s: %s", orderId, reason));
    }
}
