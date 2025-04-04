package com.first.demo.controller;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.first.demo.dto.KakaoLoginResponse;
import com.first.demo.service.KakaoOAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth2/kakao")
// 프론트엔드로부터 인가코드를 받는 Controller 
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoOAuthService;

    @GetMapping("/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code,
                                        @RequestParam("redirect_uri") String redirectUri,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        try {
            KakaoLoginResponse loginResponse = kakaoOAuthService.loginWithKakao(code, redirectUri, response);
            return ResponseEntity.ok(loginResponse); // accessToken 포함 응답
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
