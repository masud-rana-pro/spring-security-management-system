package com.secureauthlab.backend.exception;

// Custom runtime exception used to return clean, client-facing business logic error messages
public class ApiException extends RuntimeException {
    
    // Construct the exception with a specific error message
    public ApiException(String message) {
        super(message);
    }
}
