package com.first.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.first.demo.config.jwt.TokenProvider;
import com.first.demo.dao.RefreshToken;
import com.first.demo.dao.User;
import com.first.demo.dto.AddUserRequest;
import com.first.demo.dto.LoginRequest;
import com.first.demo.repository.RefreshTokenRepository;
import com.first.demo.service.UserService;

import ch.qos.logback.core.util.Duration;

@RestController // 메서드의 반환 값이 JSON,Response Body(@Controller는 View 반환)
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final TokenProvider tokenProvider; 
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 (POST /api/auth/signup)
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AddUserRequest request) {
        try {
            Long userId = userService.createUser(request);
            return ResponseEntity.ok("회원가입 성공! User ID: " + userId);
        } catch (IllegalArgumentException e) {
            // return ResponseEntity.badRequest().build();
            return ResponseEntity.badRequest().body(e.getMessage()); // 400 Bad Request : 클라이언트 요청이 잘못됨(에러 메세지 포함)
        }
    }

    // 로그인 (POST /api/auth/login)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 사용자 인증 (이메일 & 비밀번호 확인) ----------------------구현중
        // User user = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        // if (user == null) {
        //     return ResponseEntity.status(401).body("Invalid email or password");
        // }

        // // 액세세스 토큰 & 리프레시 토큰 생성
        // String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2)); // 2시간짜리 액세스 토큰
        // String refreshToken = tokenProvider.generateToken(user, Duration.ofDays(14)); // 14일짜리 리프레시 토큰

        // // Refresh Token 저장 (DB 또는 Redis)
        // refreshTokenRepository.save(new RefreshToken(user.getUserId(), refreshToken));

        // // 액세스 & 리프레시 토큰 반환
        // return ResponseEntity.ok(Map.of(
        //     "accessToken", accessToken,
        //     "refreshToken", refreshToken
        // ));
    }

}
