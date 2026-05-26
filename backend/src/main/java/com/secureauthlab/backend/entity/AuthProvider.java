package com.secureauthlab.backend.entity;

// Defines supported authentication methods
public enum AuthProvider {
    // Normal registration with email and password
    LOCAL,
    
    // Social login via Google OAuth2
    GOOGLE,
    
    // Social login via GitHub OAuth2
    GITHUB
}
