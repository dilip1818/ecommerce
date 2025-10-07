package com.ideas2it.ecommerce.dto;

import java.time.Instant;

public class ErrorResponse {
    public String message;
    public String error;
    public int status;
    public Instant timestamp;
    public String path;
    
    public ErrorResponse() {}
    
    public ErrorResponse(String message, String error, int status, String path) {
        this.message = message;
        this.error = error;
        this.status = status;
        this.timestamp = Instant.now();
        this.path = path;
    }
}
