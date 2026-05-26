package com.secureauthlab.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secureauthlab.backend.entity.User;

// Interface for performing CRUD operations on the User entity mapped to the database
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Retrieve a user from the database by their email address
    Optional<User> findByEmail(String email);

    // Check if a user with the specified email address already exists in the database
    Boolean existsByEmail(String email);
}
