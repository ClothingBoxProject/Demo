package com.first.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoLoginResponse {

    private Long uid;               // 우리 시스템의 사용자 ID
    private String nickname;        // 사용자 이름 or 닉네임
    private String email;           // 이메일
    private String accessToken;       // JWT 토큰 (access + refresh)
}
