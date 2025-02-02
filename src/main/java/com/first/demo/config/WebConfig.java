package com.first.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // React에서 처리해야 할 경로를 Spring Boot가 index.html로 전달하도록 설정
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        // "/"로 들어오는 요청을 index.html로 포워딩
        registry.addViewController("/{path:^(?!static|assets|templates|index\\.html$).*$}")
                .setViewName("forward:/index.html");
    }
    // 정적 리소스 제공(React 빌드된 정적 파일이 static 디렉토리에 들어 있음)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**") // 모든 정적 파일 요청을 처리
                .addResourceLocations("classpath:/static/"); // src/main/resources/static/ 디렉토리에서 정적 파일을 제공
    }
}
