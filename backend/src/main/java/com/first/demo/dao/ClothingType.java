package com.first.demo.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class ClothingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeId;  // 고유 ID

    @Column(nullable = false, length = 50)
    private String typeName; // 의류 카테고리 이름

    @OneToMany(mappedBy = "clothingType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetailedClothingInfo> detailedClothingInfos; // 연관된 의류 정보 리스트
}
