package com.secureauthlab.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.secureauthlab.backend.security.JwtAuthFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CorsConfig corsConfig;

    // Configure HTTP security filters, public paths, and authorization rules
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF protection since APIs use stateless token-based authorization
            .csrf(csrf -> csrf.disable())
            // Apply CORS configuration to allow cross-origin requests from frontend
            .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
            // Configure route access control policies
            .authorizeHttpRequests(auth -> {
                // Allow unrestricted access to public and authentication endpoints
                auth.requestMatchers("/api/public/**").permitAll();
                auth.requestMatchers("/api/auth/**").permitAll();
                auth.requestMatchers("/api/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");
                auth.requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN");
                // Enforce authentication on all other incoming requests
                auth.anyRequest().authenticated();
            })
            // Insert JWT authentication filter before the default username-password filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
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
