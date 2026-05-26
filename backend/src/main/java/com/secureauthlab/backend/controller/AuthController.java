package com.secureauthlab.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secureauthlab.backend.dto.AuthResponse;
import com.secureauthlab.backend.dto.RegisterRequest;
import com.secureauthlab.backend.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// Controller handling authentication endpoints such as registration and login
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Handles client request to register a new user, applying input validation
    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }
}
