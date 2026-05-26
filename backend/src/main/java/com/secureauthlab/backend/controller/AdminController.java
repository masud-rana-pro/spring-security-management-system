package com.secureauthlab.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secureauthlab.backend.entity.Role;
import com.secureauthlab.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// Controller providing administrative endpoints accessible only to ADMIN role users
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    // Returns the total count of registered users in the system
    // Only accessible by users with ADMIN authority
    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String dashboard() {
        long totalUsers = userRepository.count();
        return "Admin Dashboard - Total registered users: " + totalUsers;
    }
}