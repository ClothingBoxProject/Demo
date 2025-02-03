package com.first.demo.repository;

import com.first.demo.domain.DetailedClothingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailedClothingInfoRepository extends JpaRepository<DetailedClothingInfo, Long> {

    // 의류 카테고리 ID로 의류 상세 정보 조회
    List<DetailedClothingInfo> findByClothingType_TypeId(Long typeId);

    // 의류 이름으로 조회
    List<DetailedClothingInfo> findByDetailNameContaining(String detailName);
}
