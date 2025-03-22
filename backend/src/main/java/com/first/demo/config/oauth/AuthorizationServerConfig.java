package com.first.demo.config.oauth;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class AuthorizationServerConfig {

    @Bean
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        return http.build();
    // /oauth2/token, /oauth2/authorize, /oauth2/jwks, /oauth2/introspect 등의 엔드포인트를 자동 구성
    // 필터 체인은 Access Token을 발급하거나, 토큰 유효성 검사를 처리하는 역할 
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
        // 엔드포인트 경로 변경이 필요하다면 여기서 설정 가능 (예: authorizationEndpoint("/auth"))
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("my-client")
            .clientSecret("{noop}secret")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("http://localhost:5173/login/oauth2/code/my-client")
            .scope("read")
            .build();
            // OAuth2 클라이언트 등록 (Authorization Server에서 인가 요청을 허용할 앱)
            // 여기서는 my-client가 React 앱 또는 Postman 같은 클라이언트라고 가정
            // AuthorizationGrantType.AUTHORIZATION_CODE: 사용자 로그인을 위한 표준 인가 방식
            // AuthorizationGrantType.REFRESH_TOKEN: Access Token 만료 시 갱신
            // redirectUri: 인가 코드 발급 후 이동할 클라이언트 URL
        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    @Bean
    public RSAKey rsaKey() {
        return Jwks.generateRsa();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
        //Access Token을 JWT로 서명하기 위한 RSA 키 쌍 생성
        // JWK 형식으로 제공되어 /oauth2/jwks 경로에서 공개키 제공
        // 클라이언트나 리소스 서버가 JWT 서명을 검증할 수 있음
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    }

}
