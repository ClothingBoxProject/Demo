package com.first.demo.controller;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.demo.config.jwt.TokenProvider;
import com.first.demo.dao.RefreshToken;
import com.first.demo.dao.User;
import com.first.demo.dto.LoginRequest;
import com.first.demo.repository.RefreshTokenRepository;
import com.first.demo.service.TokenService;
import com.first.demo.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {
    
    
    @Autowired
    private MockMvc mockMvc;  // MockMvc를 @Autowired로 주입

    @MockBean
    private UserService userService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("리프레시 토큰이 없는 경우")
    void loginWithoutRefreshToken() throws Exception {
        // given
        LoginRequest request = new LoginRequest("test@example.com", "password");
        User user = UserTestFactory.createTestUser(1L, "test@example.com", "hashedPassword");
        String newRefreshToken = "new_refresh_token";
        String newAccessToken = "new_access_token";

        when(userService.authenticate(request.getEmail(), request.getPassword())).thenReturn(user);
        when(refreshTokenRepository.findByUserId(user.getUserId())).thenReturn(Optional.empty());
        // 새 리프레시 토큰 생성 및 저장
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(new RefreshToken(user.getUserId(), newRefreshToken));
        // 새로운 액세스 토큰 발급
        when(tokenService.createNewAccessToken(newRefreshToken)).thenReturn(Optional.of(newAccessToken));

        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value(newAccessToken))
            .andExpect(jsonPath("$.refreshToken").value(newRefreshToken));
    }

    @Test
    @DisplayName("리프레시 토큰이 있지만 만료된 경우")
    void loginWithExpiredRefreshToken() throws Exception {
        // given
        LoginRequest request = new LoginRequest("test@example.com", "password");
        User user = UserTestFactory.createTestUser(1L, "test@example.com", "hashedPassword");
        RefreshToken expiredRefreshToken = new RefreshToken(user.getUserId(), "expired_refresh_token");

        when(userService.authenticate(request.getEmail(), request.getPassword())).thenReturn(user);
        when(refreshTokenRepository.findByUserId(user.getUserId())).thenReturn(Optional.of(expiredRefreshToken));
        when(tokenProvider.validateToken(expiredRefreshToken.getRefreshToken())).thenReturn(TokenProvider.TokenValidationResult.EXPIRED);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(new RefreshToken(user.getUserId(), "new_refresh_token"));
        when(tokenService.createNewAccessToken("new_refresh_token")).thenReturn(Optional.of("new_access_token"));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new_access_token"))
                .andExpect(jsonPath("$.refreshToken").value("new_refresh_token"));
    }

    @Test
    @DisplayName("리프레시 토큰이 유효하고 액세스 토큰만 만료된 경우")
    void loginWithValidRefreshTokenButExpiredAccessToken() throws Exception {
        // given
        LoginRequest request = new LoginRequest("test@example.com", "password");
        User user = UserTestFactory.createTestUser(1L, "test@example.com", "hashedPassword");
        RefreshToken validRefreshToken = new RefreshToken(user.getUserId(), "valid_refresh_token");

        when(userService.authenticate(request.getEmail(), request.getPassword())).thenReturn(user);
        when(refreshTokenRepository.findByUserId(user.getUserId())).thenReturn(Optional.of(validRefreshToken));
        when(tokenProvider.validateToken(validRefreshToken.getRefreshToken())).thenReturn(TokenProvider.TokenValidationResult.VALID);
        when(tokenService.createNewAccessToken(validRefreshToken.getRefreshToken())).thenReturn(Optional.of("new_access_token"));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new_access_token"))
                .andExpect(jsonPath("$.refreshToken").value("valid_refresh_token"));
    }

    @Test
    @DisplayName("액세스 토큰과 리프레시 토큰 모두 살아있는 경우")
    void loginWithValidAccessAndRefreshTokens() throws Exception {
        // given
        LoginRequest request = new LoginRequest("test@example.com", "password");
        User user = UserTestFactory.createTestUser(1L, "test@example.com", "hashedPassword");
        RefreshToken validRefreshToken = new RefreshToken(user.getUserId(), "valid_refresh_token");

        when(userService.authenticate(request.getEmail(), request.getPassword())).thenReturn(user);
        when(refreshTokenRepository.findByUserId(user.getUserId())).thenReturn(Optional.of(validRefreshToken));
        when(tokenProvider.validateToken(validRefreshToken.getRefreshToken())).thenReturn(TokenProvider.TokenValidationResult.VALID);
        when(tokenService.createNewAccessToken(validRefreshToken.getRefreshToken())).thenReturn(Optional.of("valid_access_token"));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("valid_access_token"))
                .andExpect(jsonPath("$.refreshToken").value("valid_refresh_token"));
    }
}
