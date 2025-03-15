package com.first.demo.config.jwt;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.first.demo.dao.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    @Autowired 
    public TokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    // 토큰을 생성하는 메서드 중 노출시킬 수 있는 메서드(Public)
    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    // JWT 토큰 생성 메서드
    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 typ : JWT
                .setIssuer(jwtProperties.getIssuer()) // 발급자 (application.properties에서 설정)
                .setIssuedAt(now) // 토큰 생성 시간
                .setExpiration(expiry) // 만료 시간
                .setSubject(user.getEmail()) // sub: 유저 이메일
                .claim("id", user.getUserId()) // claim id: 유저 ID (user.getId())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()) // HMAC SHA256 방식 암호화
                .compact();
    }

    // JWT 토큰 유효성 검증 & 만료 검증 메서드 
    public TokenValidationResult validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) // 비밀키로 복호화
                    .parseClaimsJws(token);
            return TokenValidationResult.VALID; // 유효한 토큰
        } catch (ExpiredJwtException e) {
            return TokenValidationResult.EXPIRED; // 만료된 토큰
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException e) {
            return TokenValidationResult.INVALID; // 변조된 토큰
        } catch (IllegalArgumentException e) {
            return TokenValidationResult.ERROR; // 기타 에러
        }
    }

    // 토큰 상태를 Enum으로 정의
    public enum TokenValidationResult {
        VALID,       // 정상 토큰
        EXPIRED,     // 만료된 토큰
        INVALID,     // 변조된 토큰
        ERROR        // 기타 오류
    }

    // 토큰 기반으로 인증 정보 가져오는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject
            (), "", authorities), token, authorities);
    }

    // 토큰 기반으로 유저 ID 가져오는 메서드
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    // 토큰에서 Claims 정보 조회
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey()) // 비밀키로 복호화
                .parseClaimsJws(token)
                .getBody();
    }
}
 