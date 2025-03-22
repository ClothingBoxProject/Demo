// package com.first.demo.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// /*Spring Boot Backend에서 CORS(Cross-Origin Resource Sharing) 설정을 추가*/
// @Configuration
// public class CorsConfig implements WebMvcConfigurer {
//     /*CORS 설정 추가*/
//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
//                 .allowedOrigins("http://localhost:5173") // React 개발 서버 허용
//                 .allowedMethods("*") // 모든 HTTP 메서드 허용 
//                 .allowedHeaders("*") // 모든 헤더 허용
//                 .allowCredentials(true); // 인증 정보를 포함한 요청 허용
//     }
// }
