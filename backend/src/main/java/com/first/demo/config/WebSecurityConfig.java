package com.first.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import com.first.demo.service.UserDetailService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {


    // 정적 리소스 및 특정 URL 보안 예외 설정
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/static/**"); //정적 파일은 security 기능이 비활성화됨 
    }

    // HTTP 요청별 보안 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login", "/api/auth/signup").permitAll() // /login, /signup, /user → 모든 사용자 접근 가능 (permitAll())
                .anyRequest().authenticated()) // 그 외 요청 → 인증 필요 (authenticated())
                .logout(logout -> logout // 로그아웃 설정, 로그아웃 후 /login으로 이동 & 세션 무효화 
                .logoutSuccessUrl("/api/auth/login")
                .invalidateHttpSession(true))
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .build();
    }

    // 인증 매니저 및 비밀번호 암호화 설정
    @Bean
    public AuthenticationManager authenticationManager(
        HttpSecurity http, 
        BCryptPasswordEncoder bCryptPasswordEncoder, 
        UserDetailService userDetailService) throws Exception {
        
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(userDetailService); //사용자 정보를 가져올 서비스 설정
            authProvider.setPasswordEncoder(bCryptPasswordEncoder); // 비밀번호를 암호화하기 위한 인코더 설정 
            return new ProviderManager(authProvider);
    }

    // 회원가입 시 비밀번호를 암호화하여 저장하고, 로그인 시 비밀번호 검증에 사용됨.
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}