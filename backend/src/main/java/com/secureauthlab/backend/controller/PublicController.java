package com.secureauthlab.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controller for endpoints that are publicly accessible without authentication
@RestController
@RequestMapping("/api/public/")
public class PublicController {

    // Simple status check endpoint that returns a basic hello message
    @GetMapping("/hello")
    public String hello() {
        return "Hello Secure Auth Lab";
    }
}
