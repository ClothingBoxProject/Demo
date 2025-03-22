package com.first.demo.dao;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 하나의 User는 여러 개의 RefreshToken을 가질 수 있음 
    @JoinColumn(name = "user_id", nullable = false) // 사용자 정보는 실제로 필요할 때만 DB에서 로딩
    private User user;

    @Column(name = "refresh_token", nullable = false, unique = true)
    private String refreshToken;

    @Column(nullable = false)
    private boolean isActive = true; 

    @Column(nullable = false)
    private Date expiresAt;

    public RefreshToken(User user, String refreshToken, Date expiresAt) {
        this.user = user;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    public void deactivate() {
        this.isActive = false;
    }
    
    public boolean isExpired() {
        return new Date().after(this.expiresAt);
    }
}