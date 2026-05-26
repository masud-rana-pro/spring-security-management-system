package com.secureauthlab.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// Data Transfer Object containing registration or login response fields returned to the client
@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    
    // Status message indicating the result of the operation
    private String message;
    
    // Email of the authenticated user
    private String email;
    
    // Role assigned to the user
    private String role;
}
