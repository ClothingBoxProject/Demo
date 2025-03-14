package com.first.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // CSRF 보호를 비활성화하고 HTTP 보안을 설정하는 SecurityFilterChain 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/collection-bins/upload-csv") // CSV 파일 업로드 경로에 대해서만 CSRF 보호 비활성화
                )
                .authorizeRequests(authz -> authz
                        .requestMatchers("/api/collection-bins/upload-csv").permitAll()  // CSV 파일 업로드 경로는 인증 없이 접근 허용
                        .requestMatchers("/error").permitAll()  // /error 경로는 인증 없이 접근 허용
                        .anyRequest().authenticated()  // 나머지 요청은 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/login")  // 로그인 페이지를 /login 경로로 설정
                        .permitAll()  // 로그인 페이지는 모든 사용자에게 허용
                        .defaultSuccessUrl("/home", true)  // 로그인 성공 후 이동할 경로 설정
                );

        return http.build();
    }

    // 비밀번호 암호화를 위한 PasswordEncoder Bean 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
