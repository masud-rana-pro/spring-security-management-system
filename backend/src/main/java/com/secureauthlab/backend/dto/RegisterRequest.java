package com.secureauthlab.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// Data Transfer Object containing request parameters for user registration
@Setter
@Getter
public class RegisterRequest {
    
    // User name input validation, must not be empty or whitespace
    @NotBlank(message = "Name is required")
    private String name;

    // Email input validation, must not be empty and must match standard email pattern
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    // Password input validation, must not be empty and must have at least 6 characters
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}
