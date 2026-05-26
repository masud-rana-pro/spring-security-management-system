package com.secureauthlab.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Security configuration class to define endpoint access policies and encoder beans
@Configuration
public class SecurityConfig {

    // Configure HTTP security filters, public paths, and authorization rules
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF protection since APIs use stateless token-based authorization
            .csrf(csrf -> csrf.disable())
            // Configure route access control policies
            .authorizeHttpRequests(auth -> {
                // Allow unrestricted access to public and authentication endpoints
                auth.requestMatchers("/api/public/**").permitAll();
                auth.requestMatchers("/api/auth/**").permitAll();
                // Enforce authentication on all other incoming requests
                auth.anyRequest().authenticated();
            });
        return http.build();
    }

    // Encoder bean to hash and verify raw passwords securely using BCrypt algorithm
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
