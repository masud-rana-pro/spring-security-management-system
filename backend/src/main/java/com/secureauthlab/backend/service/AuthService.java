package com.secureauthlab.backend.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.secureauthlab.backend.dto.AuthResponse;
import com.secureauthlab.backend.dto.LoginRequest;
import com.secureauthlab.backend.dto.RegisterRequest;
import com.secureauthlab.backend.entity.AuthProvider;
import com.secureauthlab.backend.entity.Role;
import com.secureauthlab.backend.entity.User;
import com.secureauthlab.backend.exception.ApiException;
import com.secureauthlab.backend.repository.UserRepository;
import com.secureauthlab.backend.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException("Email already exists");
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

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();

            return new AuthResponse(
                "Login successful",
                user.getEmail(),
                user.getRole().name()
            );
        } catch (BadCredentialsException ex) {
            throw new ApiException("Invalid email or password");
        } catch (DisabledException ex) {
            throw new ApiException("Account is disabled");
        }
    }
}
