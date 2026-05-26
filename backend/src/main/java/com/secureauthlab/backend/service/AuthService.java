package com.secureauthlab.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.secureauthlab.backend.dto.AuthResponse;
import com.secureauthlab.backend.dto.RegisterRequest;
import com.secureauthlab.backend.entity.AuthProvider;
import com.secureauthlab.backend.entity.Role;
import com.secureauthlab.backend.entity.User;
import com.secureauthlab.backend.exception.ApiException;
import com.secureauthlab.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// Service containing authentication and registration business logic
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Registers a new user into the system after verifying details
    public AuthResponse register(RegisterRequest request) {
        // Enforce unique email policy across all local accounts
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException("Email already exists");
        }

        // Map request parameters to the user entity and set default attributes
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        // Encrypt password using BCrypt before storing in the database
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setProvider(AuthProvider.LOCAL);
        user.setEnabled(true);

        // Persist the user entity in the database
        User savedUser = userRepository.save(user);

        // Return registration outcome details to the client
        return new AuthResponse(
                "Registration successfull",
                savedUser.getEmail(),
                savedUser.getRole().name());
    }
}
