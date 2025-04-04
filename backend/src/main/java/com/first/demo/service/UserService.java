//UserService.java
package com.first.demo.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.first.demo.dao.User;
import com.first.demo.dto.AddUserRequest;
import com.first.demo.exception.UserServiceException;
import com.first.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// JpaRepository<User, Long>를 상속하면 일반적인 User 관리기능(CRUD:Create-Read-Update-Delete)을 제공 
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
    // 모든 사용자 조회
    public List<User> getAllUserIds() {
        return userRepository.findAll();
    }

    // 회원가입 
    public Long createUser(AddUserRequest dto) {
        userRepository.findByEmail(dto.getEmail())
            .ifPresent(user -> { // Optional을 사용하여 존재하는 경우 예외 발생
            throw new UserServiceException("이미 존재하는 이메일입니다.", HttpStatus.CONFLICT);
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

    // 회원업데이트
    public User updateUser(User user, Map<String, Object> updates) {
            updates.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(User.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, user, value);
                }
            });
            return userRepository.save(user);
    }

    // 회원삭제(회원탈퇴)
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId); // JpaRepository에서 기본적으로 제공 
    }

    // 소셜 회원가입 
    public Long createSocialUser(String email, String nickname) {
        userRepository.findByEmail(email)
            .ifPresent(user -> {
            throw new UserServiceException("이미 존재하는 이메일입니다.", HttpStatus.CONFLICT);
        });
        return userRepository.save(User.builder()
                    .email(email)
                    .userName(nickname)
                    .passwordHash("SOCIAL_USER") 
                    .build()).getUserId();
    }
    
}

