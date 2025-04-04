package com.first.demo.config.jwt;

import java.time.Duration;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.first.demo.dao.User;
import com.first.demo.service.CustomUserDetailsService;

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
    private final CustomUserDetailsService userDetailService;

    @Autowired // @Autowired를 사용하면, Spring이 JwtProperties를 찾아서 자동으로 주입
    public TokenProvider(JwtProperties jwtProperties, CustomUserDetailsService userDetailService) {
        this.jwtProperties = jwtProperties;
        this.userDetailService = userDetailService;
    }

    // 토큰을 생성하는 메서드
    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    // JWT 토큰 생성 메서드
    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 설정(typ : JWT)
                .setIssuer(jwtProperties.getIssuer()) // 발급자 설정(application.properties에서 설정)
                .setIssuedAt(now) // 토큰 생성 시간(iat)
                .setExpiration(expiry) //토큰 만료 시간(exp)
                .setSubject(user.getEmail())
                .claim("id", user.getUserId()) // 유저 아이디(client_id)
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

    // 토큰 기반으로 인증 정보 가져와서 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        String email = getSubject(token);
        UserDetails userDetails = userDetailService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
        // UsernamePasswordAuthenticationToken을 사용하여 SecurityContext에서 인증 가능하도록 객체 생성
    }

    // 토큰 기반으로 유저 ID 가져오는 메서드
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }
    // 토큰으로 만료 기한 가져오는 메서드 
    public Date getExpiration(String token) {
        return getClaims(token).getExpiration();
    }
    // 토큰으로 이메일 가져오는 메서드 
    public String getSubject(String token){
        return getClaims(token).getSubject();
    }
    // 토큰에서 Claims 정보 조회
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey()) // 비밀키로 복호화
                .parseClaimsJws(token)
                .getBody();
    }

}
 