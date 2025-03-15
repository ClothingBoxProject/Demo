package com.first.demo.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.first.demo.config.jwt.TokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// 토큰 필터 구현
// (JWT 토큰을 Authorization 헤더에 포함하여 요청하면 이를 검사하고 SecurityContext에 인증 정보를 설정)
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    // 사용자가 JWT를 헤더에 포함하여 요청
    // JWT가 유효한지 검증한 후 인증 정보를 SecurityContext에 설정
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)  throws ServletException, IOException {
        // 요청 헤더의 Authorization 키의 값 조회 
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        // 가져온 값에서 접두사를 제거하고 jwt 토큰만 남기기 
        String token = getAccessToken(authorizationHeader);
        TokenProvider.TokenValidationResult result = tokenProvider.validateToken(token);

        switch (result) {
            case VALID -> {
                Authentication authentication = tokenProvider.getAuthentication(token); // 토큰 기반으로 인증 정보 가져오는 메서드
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Spring Security의 SecurityContext에 Authentication 객체를 저장
            }
            case EXPIRED -> {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 🔥 401 응답 반환 (액세스 토큰 만료)
                response.getWriter().write("Access token expired");
                return;
            }
            case INVALID -> {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 🔥 401 응답 반환 (변조된 토큰)
                response.getWriter().write("Invalid token");
                return;
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + result);
        }

        filterChain.doFilter(request, response);
        // 인증이 완료된 상태로 요청을 다음 필터 또는 컨트롤러로 전달 
    }

    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
