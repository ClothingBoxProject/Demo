//UserService.java
package com.first.demo.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.first.demo.dao.User;
import com.first.demo.dto.AddUserRequest;
import com.first.demo.repository.UserRepository;

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

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    // 회원가입 
    public Long createUser(AddUserRequest dto) {
        userRepository.findByEmail(dto.getEmail())
            .ifPresent(user -> { // Optional을 사용하여 존재하는 경우 예외 발생
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        });
        // 이메일 중복 체크
        return userRepository.save(User.builder()
                    .email(dto.getEmail())
                    .passwordHash(bCryptPasswordEncoder.encode(dto.getPassword()))
                    .build()).getUserId();
        // save 메서드가 User 엔티티를 반환 -> User 엔티티에서 UserId를 빼내기 
    }
 
    // 로그인
    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 이메일입니다.")); // ERR(404): 사용자가 존재하지 않음

        if (!bCryptPasswordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다."); // ERR(401): 비밀번호 불일치
        }
        return user; // 정상적인 경우 User 객체 반환
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}

