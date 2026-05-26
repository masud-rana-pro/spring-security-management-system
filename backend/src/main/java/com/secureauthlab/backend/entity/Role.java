package com.secureauthlab.backend.entity;

// Defines user authorization roles in the application
public enum Role {
    // Has full access to administrative endpoints and data
    ADMIN,
    
    // Has access to standard client endpoints and personal data
    USER
}
