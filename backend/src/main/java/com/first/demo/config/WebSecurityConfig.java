//WebSecurityConfig.java
package com.first.demo.config;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.first.demo.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // Spring Security 적용 설정 
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/static/**"); //정적 파일은 security 기능이 비활성화됨(아래의 filterchain을 아예 안 거침)
    }

    // CORS 설정 
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5174", "http://localhost:5173")); // React 개발 서버 허용
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH")); // 허용할 HTTP 메서드
        configuration.setAllowedHeaders(List.of("*")); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 인증 정보 포함한 요청 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 적용
        return source;
    }

    // Spring Security HTTP 요청별 보안 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtRequestFilter jwtRequestFilter) throws Exception {
        // HttpSecurity : Spring Security에서 제공하는 보안 관련 설정을 구성하는 클래스(Spring Security의 기본 보안 설정을 재정의)
        return http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) 
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/donations/**", "/api/users/**", "/api/suggestions/**").authenticated() // 로그인(또는 토큰 인증 등)한 사용자만 해당 요청을 허용
            .anyRequest().permitAll() // 인증/인가 체크 없이 모든 사용자에게 허용
            )
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class) // jwtRequestFilter를 UsernamePasswordAuthenticationFilter 이전에 실행되도록 설정
            .build();
            //인증이 필요한 API는 SecurityFilterChain에 의해 jwtRequestFilter가 실행
    }
    
    // 인증 매니저 및 비밀번호 암호화 설정
    @Bean
    public AuthenticationManager authenticationManager(
        HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, 
        CustomUserDetailsService userDetailService) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        // HttpSecurity에서 AuthenticationManagerBuilder 객체를 가져오는 것
        authenticationManagerBuilder.userDetailsService(userDetailService) // 재정의한 userDetailService를 사용하도록 설정 
            .passwordEncoder(bCryptPasswordEncoder());  // 비밀번호를 암호화하기 위한 인코더 설정 
        return authenticationManagerBuilder.build();
        // AuthenticationManager최종적으로 build 
    }

    // 비밀번호 암호화 설정 
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}