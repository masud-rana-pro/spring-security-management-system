package com.secureauthlab.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secureauthlab.backend.security.CustomUserDetails;

// Controller providing profile and user-specific endpoints for authenticated users
@RestController
@RequestMapping("/api/user")
public class UserController {

    // Returns the profile information of the currently authenticated user
    // Accessible by both USER and ADMIN role users
    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public String profile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return "User Profile - Email: " + userDetails.getUsername()
                + ", Name: " + userDetails.getUser().getName()
                + ", Role: " + userDetails.getUser().getRole();
    }
}