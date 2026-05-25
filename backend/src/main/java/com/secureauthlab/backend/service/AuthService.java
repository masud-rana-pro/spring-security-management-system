package com.secureauthlab.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.secureauthlab.backend.dto.AuthResponse;
import com.secureauthlab.backend.dto.RegisterRequest;
import com.secureauthlab.backend.entity.AuthProvider;
import com.secureauthlab.backend.entity.Role;
import com.secureauthlab.backend.entity.User;
import com.secureauthlab.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setProvider(AuthProvider.LOCAL);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        return new AuthResponse(
                "Registration successfull",
                savedUser.getEmail(),
                savedUser.getRole().name());

    }
}
