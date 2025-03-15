//AuthController.java
package com.first.demo.controller;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

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
import com.first.demo.service.TokenService;
import com.first.demo.service.UserService;

@RestController // 메서드의 반환 값이 JSON,Response Body(@Controller는 View 반환)
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;
    private final TokenProvider tokenProvider; 
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public AuthController(UserService userService, TokenProvider tokenProvider, TokenService tokenService, RefreshTokenRepository refreshTokenRepository) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.tokenService = tokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // 회원가입 (POST /api/auth/signup)
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AddUserRequest request) {
        try {
            Long userId = userService.createUser(request);
            return ResponseEntity.ok("회원가입 성공! User ID: " + userId);
        } catch (IllegalArgumentException e) {
            // return ResponseEntity.badRequest().build();
            return ResponseEntity.status(400).body("중복된 이메일입니다."); // 400 Bad Request : 클라이언트 요청이 잘못됨(에러 메세지 포함)
        }
    }

    // 로그인 (POST /api/auth/login)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

            // 기존 Refresh Token 확인 및 만료 검증
            RefreshToken refreshTokenEntity = refreshTokenRepository.findByUserId(user.getUserId())
                .filter(token -> tokenProvider.validateToken(token.getRefreshToken()) == TokenProvider.TokenValidationResult.VALID)
                .orElseGet(() -> refreshTokenRepository.save(new RefreshToken(user.getUserId(), tokenProvider.generateToken(user, Duration.ofDays(14)))));

            // 새 Access Token 발급
            Optional<String> accessToken = tokenService.createNewAccessToken(refreshTokenEntity.getRefreshToken());
            if (accessToken.isEmpty()) {
                return ResponseEntity.status(401).body("엑세스 토큰을 발급할 수 없습니다!"); // ERR(401): Access Token 발급 불가능한 경우
            }

            return ResponseEntity.ok(Map.of(
                "accessToken", accessToken.get()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage()); // ERR(401) : 매칭이 안되는 경우 
        }
    }
}
