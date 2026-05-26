package com.secureauthlab.backend.exception;

public class ApiException extends RuntimeException {
    private int status;

    public ApiException(String message) {
        super(message);
    }
}
