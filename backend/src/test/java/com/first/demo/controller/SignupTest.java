package com.first.demo.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.demo.dto.AddUserRequest;
import com.first.demo.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class SignupTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;  // 가짜 UserService 사용

    @Autowired
    private ObjectMapper objectMapper;  // JSON 변환을 위한 ObjectMapper

    @Test
    void 회원가입_성공() throws Exception {
        // Given
        AddUserRequest request = new AddUserRequest("testuser2@example.com", "testpassWword");

        // Mock UserService 동작 정의
        Mockito.when(userService.createUser(Mockito.any(AddUserRequest.class))).thenReturn(1L);

        // When & Then
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("회원가입 성공! User ID: 1")); // 응답 검증
    }

    @Test
    void 회원가입_실패_이메일중복() throws Exception {
        // Given
        AddUserRequest request = new AddUserRequest("existing@example.com", "password123");

        // Mock UserService에서 중복 에러 발생하도록 설정
        Mockito.when(userService.createUser(Mockito.any(AddUserRequest.class)))
                .thenThrow(new IllegalArgumentException("이미 존재하는 이메일입니다."));

        // When & Then
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()) // 400 Bad Request 예상
                .andExpect(jsonPath("$").value("이미 존재하는 이메일입니다.")); // 에러 메시지 검증
    }
}
