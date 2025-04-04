package com.first.demo.service;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.demo.dao.User;
import com.first.demo.dto.KakaoLoginResponse;
import com.first.demo.dto.KakaoResponse;
import com.first.demo.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class KakaoOAuthService {
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserRepository userRepository;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    
    @Value("${kakao.key.client-id}")
    private String clientId;

    public KakaoLoginResponse loginWithKakao(String code, String redirectUri, HttpServletResponse response) {
        String kakaoAccessToken = getAccessToken(code, redirectUri);
        HashMap<String, Object> userInfo = getKakaoUserInfo(kakaoAccessToken);
        return kakaoUserLogin(userInfo, response);
    }

    private String getAccessToken(String code, String redirectUri) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<KakaoResponse> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token", 
                request, 
                KakaoResponse.class);
        return response.getBody().getAccessToken();
    }

    private HashMap<String, Object> getKakaoUserInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
    
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
    
        HttpEntity<Void> request = new HttpEntity<>(headers);
    
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                request,
                String.class
        );
    
        // JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(response.getBody());
            System.out.println("카카오 응답 JSON: " + response.getBody()); // 디버깅용
    
            JsonNode idNode = jsonNode.get("id");
            JsonNode properties = jsonNode.get("properties");
            JsonNode kakaoAccount = jsonNode.get("kakao_account");
    
            if (idNode == null || properties == null || kakaoAccount == null) {
                throw new RuntimeException("카카오 사용자 정보 누락: " + response.getBody());
            }
    
            JsonNode nicknameNode = properties.get("nickname");
            JsonNode emailNode = kakaoAccount.get("email");
    
            if (nicknameNode == null || emailNode == null || emailNode.isNull()) {
                throw new RuntimeException("카카오 사용자 nickname/email 누락: " + response.getBody());
            }
    
            userInfo.put("id", idNode.asLong());
            userInfo.put("nickname", nicknameNode.asText());
            userInfo.put("email", emailNode.asText());
            return userInfo;
    
        } catch (JsonProcessingException e) {
            throw new RuntimeException("카카오 사용자 정보 파싱 실패", e);
        }
    }
    
    private KakaoLoginResponse kakaoUserLogin(HashMap<String, Object> userInfo, HttpServletResponse response) {
        Long kakaoId = Long.valueOf(userInfo.get("id").toString());
        String email = userInfo.get("email").toString();
        String nickname = userInfo.get("nickname").toString();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            Long userId = userService.createSocialUser(email, nickname);
            user = userRepository.findById(userId).orElseThrow();
        }

        String accessToken = refreshTokenService.login(user, response);
        // accessToken만 응답에 담아 보냄
        return new KakaoLoginResponse(kakaoId, nickname, email, accessToken);
    }
}
