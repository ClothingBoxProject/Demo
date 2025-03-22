//RefreshTokenService.java 
package com.first.demo.service;

import java.time.Duration;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first.demo.config.jwt.TokenProvider;
import com.first.demo.dao.RefreshToken;
import com.first.demo.dao.User;
import com.first.demo.exception.RefreshTokenException;
import com.first.demo.repository.RefreshTokenRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private static final int REFRESH_TIME = 2*60; // Refresh Token 쿠키 만료
    private static final int ACCESS_TIME = 60; // Access Token 쿠키 만료 

    // 문자열 Refresh Token으로 엔티티 RefreshToken 리턴하기 
    private Optional<RefreshToken> findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken);
    }

    // 로그인 서비스 
    @Transactional
    public String login(User user, HttpServletResponse response){
        // 일단 기존에 있던 refreshtoken은 삭제 
        refreshTokenRepository.findAllByUser(user)
            .ifPresent(tokens -> tokens.forEach(refreshTokenRepository::delete));

        // 새 Refresh Token 값 생성 
        String newRefreshValue = tokenProvider.generateToken(user, Duration.ofSeconds(REFRESH_TIME)); 
        // Http-Only Cookie로 새로운 Refresh Token 생성 및 저장
        RefreshToken newToken = new RefreshToken(user, newRefreshValue, tokenProvider.getExpiration(newRefreshValue));
        refreshTokenRepository.save(newToken);
        // Cookie에 새로운 Refresh Token 지정 
        setRefreshTokenCookie(response, newRefreshValue, REFRESH_TIME);
        // 새로운 Access Token 생성 및 반환
        return tokenProvider.generateToken(user, Duration.ofSeconds(ACCESS_TIME)); 
    }

    // 로그아웃 서비스 
    @Transactional
    public void logout(String refreshToken, HttpServletResponse response){
        RefreshToken refreshTokenEntity = findByRefreshToken(refreshToken)
            .orElseThrow(() -> new RefreshTokenException("Refresh Token이 제공되지 않았습니다.", HttpStatus.BAD_REQUEST));
        User user = refreshTokenEntity.getUser();
        refreshTokenRepository.deleteByUser(user); // Refresh Token 쿠키 삭제(한 User의 모든 RefreshToken 삭제)
        setRefreshTokenCookie(response, null, 0);
    }

    // Access Token 재발급
    public String refreshAccessToken(String refreshToken, HttpServletResponse response) {
        Long userId = tokenProvider.getUserId(refreshToken); // refreshToken에 해당하는 ID를 가져오기
    
        RefreshToken refreshTokenEntity = findByRefreshToken(refreshToken) // 1. 존재하지 않는 refreshToken 
            .orElseThrow(() -> new RefreshTokenException("Refresh Token이 제공되지 않았습니다.", HttpStatus.BAD_REQUEST));
        
        if (!refreshTokenEntity.isActive() || refreshTokenEntity.isExpired()) { // 2. 만료된 경우에 시도(일반적), 비활성화된 경우에 시도(탈취)
            throw new RefreshTokenException("Refresh Token이 만료되었거나 비활성화되었습니다.", HttpStatus.UNAUTHORIZED);
        }
        
        User user = userService.getUserById(userId) // 3. 존재하지 않는 user 
            .orElseThrow(() -> new RefreshTokenException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        
        RefreshToken updatedRefreshToken = updateRefreshToken(refreshTokenEntity, user); 
        setRefreshTokenCookie(response, updatedRefreshToken.getRefreshToken(), REFRESH_TIME); // Http-Only Cookie로 새로운 Refresh Token 저장
        return tokenProvider.generateToken(user, Duration.ofSeconds(ACCESS_TIME)); // 새로운 Access Token 생성 및 반환
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken, int time) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        // cookie.setSecure(true); // HTTPS 환경에서만 전송 권장
        cookie.setPath("/");
        cookie.setMaxAge(time); 
        response.addCookie(cookie);
    }

    @Transactional
    private RefreshToken updateRefreshToken(RefreshToken existingToken, User user) {
        existingToken.deactivate(); // 기존 토큰 비활성화
        
        String newRefreshToken = tokenProvider.generateToken(user, Duration.ofSeconds(REFRESH_TIME));
        
        return refreshTokenRepository.save(
            new RefreshToken(user, newRefreshToken, tokenProvider.getExpiration(newRefreshToken))
        );
    }

}
