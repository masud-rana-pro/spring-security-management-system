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

@RestControllerAdvice
public class GlobalExceptionHandler {
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

    @ExceptionHandler(MethodArgumentNotValidException.class) 
    public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex,
        HttpServletRequest req
    ) {
        Map<String, String> validationErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(err -> validationErrors.put(err.getField(), err.getDefaultMessage()));
        
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
