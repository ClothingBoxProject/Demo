package com.first.demo.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class DetailedClothingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailId;  // 의류 고유 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)  // type_id를 외래키로 설정
    private ClothingType clothingType; // 의류 카테고리와 연결

    @Column(nullable = false, length = 100)
    private String detailName; // 의류 이름

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descriptionDispose; // 폐기 처리 방법

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descriptionStatus; // 의류 상태

    @Column(length = 255)
    private String imageUrl; // 의류 이미지 URL

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 등록일

    @Column(nullable = false)
    private LocalDateTime modifiedAt; // 수정일

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
}
