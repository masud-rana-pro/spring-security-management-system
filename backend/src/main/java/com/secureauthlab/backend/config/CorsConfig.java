package com.secureauthlab.backend.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// Configuration class that defines Cross-Origin Resource Sharing (CORS) settings
// CORS is a security mechanism that allows or blocks requests from different origins
@Configuration
public class CorsConfig {

    // Reads the allowed origin URL from application properties, defaults to Angular dev server
    @Value("${spring.web.cors.allowed-origins:http://localhost:4200}")
    private String allowedOrigins;

    // Provides a CorsConfigurationSource with the defined CORS policy settings
    // This source is used by both CorsFilter bean and SecurityConfig for applying CORS rules
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Define CORS rules and permitted options
        CorsConfiguration config = new CorsConfiguration();
        
        // Specify which frontend origins are allowed to access the backend API
        // The value is read from application.properties and can be a comma-separated list
        config.setAllowedOriginPatterns(Arrays.asList(allowedOrigins.split(",")));
        
        // Allow standard HTTP methods that the API supports
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        
        // Allow all standard HTTP headers in cross-origin requests
        config.setAllowedHeaders(Arrays.asList("*"));
        
        // Allow credentials like cookies, authorization headers to be sent cross-origin
        config.setAllowCredentials(true);
        
        // Expose the Authorization header so frontend can read JWT token from response
        config.setExposedHeaders(Arrays.asList("Authorization"));
        
        // Apply these CORS rules to all API endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return source;
    }

    // Creates a CorsFilter bean that intercepts incoming requests and applies CORS policy
    // This is a standard filter that can be registered globally
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
}