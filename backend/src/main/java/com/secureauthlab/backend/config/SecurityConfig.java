package com.secureauthlab.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    // Provider that uses CustomUserDetailsService and PasswordEncoder for database authentication
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    // Exposes AuthenticationManager to be injected in services for authenticating login requests
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}
