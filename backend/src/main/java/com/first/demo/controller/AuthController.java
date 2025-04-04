//AuthController.java
package com.first.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.first.demo.dao.CustomUserDetails;
import com.first.demo.dto.AddUserRequest;
import com.first.demo.dto.LoginRequest;
import com.first.demo.exception.RefreshTokenException;
import com.first.demo.service.RefreshTokenService;
import com.first.demo.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController // 메서드의 반환 값이 JSON,Response Body(@Controller는 View 반환)
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserService userService, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
    }

    // 회원가입 (POST /api/auth/signup)
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AddUserRequest request) {
        try {
            Long userId = userService.createUser(request);
            return ResponseEntity.ok("회원가입 성공! User ID: " + userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("중복된 이메일입니다."); // 400 Bad Request : 클라이언트 요청이 잘못됨(에러 메세지 포함)
        } catch (Exception e) { 
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    
    // 로그인 (POST /api/auth/login)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String accessToken = refreshTokenService.login(userDetails.getUser(), response);
            return ResponseEntity.ok(Map.of("accessToken", accessToken));

        } catch (BadCredentialsException e) {  
            return ResponseEntity.status(404).body("이메일 또는 비밀번호가 잘못되었습니다.");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // AccessToken 재발급 (POST /api/auth/refresh)
    // @CookieValue : 클라이언트가 보낸 쿠키에서 refreshToken값 추출 
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "refreshToken", required = true) String refreshToken,
        HttpServletResponse response) {
        try {
            String newAccessToken = refreshTokenService.refreshAccessToken(refreshToken, response);
            // 새로운 Access Token만 반환 (Refresh Token은 쿠키로 자동 전송)
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (RefreshTokenException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    // 로그아웃 (DELETE /api/auth/logout)
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(value = "refreshToken", required=false) String refreshToken,
        HttpServletResponse response) {
        try {
            refreshTokenService.logout(refreshToken, response);
            return ResponseEntity.ok("로그아웃 성공");
        } catch (RefreshTokenException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}
