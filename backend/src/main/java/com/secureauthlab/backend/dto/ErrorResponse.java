package com.secureauthlab.backend.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// Standardized error response format returned globally for any exception or validation failure
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    
    // The exact time when the error occurred
    private LocalDateTime timestamp;
    
    // HTTP status code representation (e.g. 400, 500)
    private int status;
    
    // HTTP status reason phrase (e.g. Bad Request, Internal Server Error)
    private String error;
    
    // Detailed message describing the reason for the error
    private String message;
    
    // The API endpoint URL that triggered the error
    private String path;
    
    // Key-value pairs representing specific validation errors on fields
    private Map<String, String> validationErrrors;
}
