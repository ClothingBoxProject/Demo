package com.first.demo.config;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.first.demo.config.jwt.TokenProvider;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class TokenAuthenticationFilterTest {

    private JwtRequestFilter tokenAuthenticationFilter;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private Authentication authentication;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tokenProvider = mock(TokenProvider.class);
        tokenAuthenticationFilter = new JwtRequestFilter(tokenProvider);
    }
    @Test
    @DisplayName("유효한 토큰이 제공될 경우 SecurityContext에 인증 정보 저장")
    void validToken_shouldAuthenticateUser() throws ServletException, IOException {
        // Given
        String validToken = "valid.token.value";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + validToken);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        when(tokenProvider.validateToken(validToken)).thenReturn(TokenProvider.TokenValidationResult.VALID);
        when(tokenProvider.getAuthentication(validToken)).thenReturn(authentication);

        // When
        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
        verify(tokenProvider, times(1)).getAuthentication(validToken);
    }

    @Test
    @DisplayName("만료된 토큰이 제공될 경우 401 Unauthorized 응답 반환")
    void expiredToken_shouldReturnUnauthorized() throws ServletException, IOException {
        // Given
        String expiredToken = "expired.token.value";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + expiredToken);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        when(tokenProvider.validateToken(expiredToken)).thenReturn(TokenProvider.TokenValidationResult.EXPIRED);

        // When
        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("Access token expired", response.getContentAsString());
    }

    @Test
    @DisplayName("변조된 토큰이 제공될 경우 401 Unauthorized 응답 반환")
    void invalidToken_shouldReturnUnauthorized() throws ServletException, IOException {
        // Given
        String invalidToken = "invalid.token.value";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + invalidToken);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        when(tokenProvider.validateToken(invalidToken)).thenReturn(TokenProvider.TokenValidationResult.INVALID);

        // When
        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("Invalid token", response.getContentAsString());
    }

    @Test
    @DisplayName("❌ Authorization 헤더가 없을 경우 필터 통과 (비인증 사용자)")
    void noToken_shouldPassThrough() throws ServletException, IOException {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        // When
        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(HttpServletResponse.SC_OK, response.getStatus()); // 필터 통과
    }
}
