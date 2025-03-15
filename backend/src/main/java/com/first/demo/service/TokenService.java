//TokenService.java
package com.first.demo.service;

import java.time.Duration;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.first.demo.config.jwt.TokenProvider;
import com.first.demo.dao.RefreshToken;
import com.first.demo.dao.User;
import com.first.demo.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<String> createNewAccessToken(String refreshToken) {
        return refreshTokenService.findByRefreshToken(refreshToken)
            .flatMap(existingToken -> {  
                Long userId = existingToken.getUserId();
                return userService.getUserById(userId) // Optional<User> 반환
                    .map(user -> tokenProvider.generateToken(user, Duration.ofHours(2))); // Optional<String> 반환
            });
    }
    // 사용자의 리프레시 토큰을 가져오거나, 만료된 경우 새로 생성하여 저장하는 기능
    public RefreshToken getOrCreateRefreshToken(Long userId, User user) {
        return refreshTokenRepository.findByUserId(userId)
                .filter(token -> tokenProvider.validateToken(token.getRefreshToken()) == TokenProvider.TokenValidationResult.VALID)
                .orElseGet(() -> refreshTokenRepository.save(new RefreshToken(userId, tokenProvider.generateToken(user, Duration.ofDays(14)))));
                //orElseGet : 반환 타입이 T이므로 Optional을 제거하고 RefreshToken을 반환
    // 리프레시 토큰이 없거나 만료된 경우 새로 생성
    }
}