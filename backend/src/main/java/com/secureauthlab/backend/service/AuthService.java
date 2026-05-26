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
import com.secureauthlab.backend.security.JwtUtil;

import lombok.RequiredArgsConstructor;

// Service containing authentication and registration business logic
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

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

        // Return registration outcome details to the client without any token
        return new AuthResponse(
                "Registration successfull",
                savedUser.getEmail(),
                savedUser.getRole().name(),
                null);
    }

    // Authenticates user credentials and returns login result with user details
    public AuthResponse login(LoginRequest request) {
        try {
            // Create authentication token with raw email and password from the request
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Set the authenticated security context for the current session
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Extract the authenticated user details from the principal object
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();

            // Generate a signed JWT containing user email and role for subsequent requests
            String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

            // Return login success response with email, role, and the generated access token
            return new AuthResponse(
                "Login successful",
                user.getEmail(),
                user.getRole().name(),
                accessToken
            );
        } catch (BadCredentialsException ex) {
            // Throw a generic error message instead of revealing which field is wrong
            throw new ApiException("Invalid email or password");
        } catch (DisabledException ex) {
            // Notify the client that the account is not active
            throw new ApiException("Account is disabled");
        }
    }
}
