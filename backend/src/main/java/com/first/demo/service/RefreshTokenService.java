package com.first.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.first.demo.dao.RefreshToken;
import com.first.demo.repository.RefreshTokenRepository;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
