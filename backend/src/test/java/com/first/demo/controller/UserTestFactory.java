package com.first.demo.controller;

import com.first.demo.dao.User;

public class UserTestFactory {

    public static User createTestUser(Long id, String email, String passwordHash) {
        return User.builder()
                .userId(id)
                .email(email)
                .passwordHash(passwordHash)
                .build();
    }
}
