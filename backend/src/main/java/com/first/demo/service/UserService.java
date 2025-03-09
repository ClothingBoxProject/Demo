package com.first.demo.service;

import com.first.demo.dao.User;
import com.first.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.first.demo.dto.AddUserRequest;

import lombok.RequiredArgsConstructor;

// 일반적인 User 관리기능(CRUD)을 제공 
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected User"));
    }

    public Long createUser(AddUserRequest dto) {
        
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        // 이메일 중복 체크
        return userRepository.save(User.builder()
                    .email(dto.getEmail())
                    .passwordHash(bCryptPasswordEncoder.encode(dto.getPassword()))
                    .build()).getUserId();
        // save메서드가 User 엔티티를 반환 -> User 엔티티에서 UserId를 빼내기 
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}

