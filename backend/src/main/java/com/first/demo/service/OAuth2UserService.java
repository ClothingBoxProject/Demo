package com.first.demo.service;

import com.first.demo.dao.User;
import com.first.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId(); // naver, kakao, google
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email;
        String name;

        if ("kakao".equals(provider)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            email = (String) kakaoAccount.get("email");
            name = (String) profile.get("nickname");
        } else if ("naver".equals(provider)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            email = (String) response.get("email");
            name = (String) response.get("name");
        } else if ("google".equals(provider)) {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
        } else {
            email = null;
            name = null;
        }

        String role = switch (provider.toLowerCase()) {
            case "kakao" -> "KAKAO_USER";
            case "naver" -> "NAVER_USER";
            case "google" -> "GOOGLE_USER";
            default -> "USER";
        };

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder()
                        .userName(name)
                        .email(email)
                        .passwordHash("OAUTH_USER")
                        .role(role)
                        .isActive(true)
                        .build()));

        Map<String, Object> simplifiedAttributes = Map.of(
                "email", email,
                "name", name,
                "provider", provider
        );
        return new DefaultOAuth2User(Set.of(), simplifiedAttributes, "email");
    }
}
