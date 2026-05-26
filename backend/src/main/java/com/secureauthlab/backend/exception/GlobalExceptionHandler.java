package com.secureauthlab.backend.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.secureauthlab.backend.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

// Catch all controller exceptions globally and format them as unified JSON error responses
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle custom ApiException thrown by our services when business logic checks fail
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage(),
            req.getRequestURI(),
            null
        );
        return new ResponseEntity<>(
            response,
            HttpStatus.BAD_REQUEST
        );
    }

    // Handle payload validation exceptions (e.g. invalid email format or empty name)
    @ExceptionHandler(MethodArgumentNotValidException.class) 
    public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex,
        HttpServletRequest req
    ) {
        Map<String, String> validationErrors = new HashMap<>();

        // Extract field validation failure messages and map them to their corresponding field names
        ex.getBindingResult().getFieldErrors().forEach(err -> 
            validationErrors.put(err.getField(), err.getDefaultMessage())
        );
        
        ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            "Validation failed",
            req.getRequestURI(),
            validationErrors
        );
        return new ResponseEntity<>(
            response,
            HttpStatus.BAD_REQUEST
        );
    }

    // Catch-all handler for any unhandled exceptions to prevent leaking internal stack traces to clients
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException (
        Exception ex,
        HttpServletRequest req
    ) {
        ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            "Something went wrong. Please try again later.",
            req.getRequestURI(),
            null
        );
        return new ResponseEntity<>(
            response,
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
} 
