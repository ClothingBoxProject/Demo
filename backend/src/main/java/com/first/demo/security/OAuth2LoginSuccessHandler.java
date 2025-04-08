package com.first.demo.security;

import com.first.demo.config.jwt.TokenProvider;
import com.first.demo.dao.User;
import com.first.demo.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("✅ OAuth 로그인 성공");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("🔍 OAuth2User attributes: {}", oAuth2User.getAttributes());

        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            Map<String, Object> responseMap = (Map<String, Object>) oAuth2User.getAttribute("response");
            email = responseMap != null ? (String) responseMap.get("email") : null;
        }

        log.info("📧 가져온 이메일: {}", email);

        if (email == null) {
            log.error("❌ OAuth2 로그인 실패: 이메일 정보가 없습니다.");
            throw new IllegalArgumentException("OAuth2 로그인 실패: 이메일 정보가 없습니다.");
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            log.error("❌ OAuth2 로그인 실패: 등록되지 않은 사용자입니다.");
            throw new IllegalArgumentException("OAuth2 로그인 실패: 등록되지 않은 사용자입니다.");
        }

        User user = optionalUser.get();
        String jwtToken = tokenProvider.generateToken(user, Duration.ofHours(1));

        log.info("✅ JWT 발급 완료: {}", jwtToken);

        // ✅ JWT를 쿠키로 저장 (보안 강화)
        Cookie jwtCookie = new Cookie("accessToken", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true); //  Https 환경에서만 브라우저가 쿠키를 서버에 전송하게! (https 도메인 연결하면 자동으로 브라우저가 안전하게 처리해줌)
        // 지금은 localhost라 로그인 후에 개발자 도구 쿠키에 액세스 토큰이 보이고 있음
        // HttpOnly 설정해둬서 JS에서 document.cookie로 접근 불가하니 보여도 상관 없다고는 하는데 일단 안 보이는 게 나을 것 같아서!
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(3600); // 1시간
        response.addCookie(jwtCookie);

        // ✅ OAuth 로그인 성공 후 프론트엔드로 이동 (쿼리 파라미터에 토큰 포함)
        response.sendRedirect("http://localhost:5173/oauth-success?token=" + jwtToken);
    }
}
