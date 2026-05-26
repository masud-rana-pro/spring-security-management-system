package com.secureauthlab.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

// Map this class to a database table named users
@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {

    // Primary key with auto-increment strategy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Full name of the user, cannot be null
    @Column(nullable = false)
    private String name;

    // Email address used for registration/login, must be unique and have maximum length of 150
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    // Encrypted password, nullable because OAuth2/social users do not have a password
    @Column(nullable = true)
    private String password;

    // Store enum values as String in the database table (e.g. USER, ADMIN)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    // Authentication provider, stores LOCAL, GOOGLE, or GITHUB
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuthProvider provider;

    // Flag to indicate if the user account is active
    @Column(nullable = false)
    private Boolean enabled = true;

    // Timestamp showing when the user record was created
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // JPA hook executed automatically before saving a new record to the database
    @PrePersist
    public void beforeSave() {
        this.createdAt = LocalDateTime.now();

        // Assign default role if none is specified
        if (this.role == null) {
            this.role = Role.USER;
        }

        // Assign default local provider if none is specified
        if (this.provider == null) {
            this.provider = AuthProvider.LOCAL;
        }

        // Set account status to active if not explicitly specified
        if (this.enabled == null) {
            this.enabled = true;
        }
    }
}
