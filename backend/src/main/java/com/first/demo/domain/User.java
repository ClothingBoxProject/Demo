package com.first.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false, length = 20)
    private String role;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
}