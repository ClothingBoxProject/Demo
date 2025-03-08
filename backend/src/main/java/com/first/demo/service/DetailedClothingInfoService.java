package com.first.demo.service;

import com.first.demo.domain.DetailedClothingInfo;
import com.first.demo.repository.DetailedClothingInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetailedClothingInfoService {

    private final DetailedClothingInfoRepository detailedClothingInfoRepository;

    @Autowired
    public DetailedClothingInfoService(DetailedClothingInfoRepository detailedClothingInfoRepository) {
        this.detailedClothingInfoRepository = detailedClothingInfoRepository;
    }

    // 모든 의류 상세 정보 조회
    public List<DetailedClothingInfo> getAllDetailedClothingInfos() {
        return detailedClothingInfoRepository.findAll();
    }

    // 의류 ID로 의류 상세 정보 조회
    public Optional<DetailedClothingInfo> getDetailedClothingInfoById(Long detailId) {
        return detailedClothingInfoRepository.findById(detailId);
    }

    // 의류 카테고리 ID로 의류 상세 정보 조회
    public List<DetailedClothingInfo> getDetailedClothingInfosByTypeId(Long typeId) {
        return detailedClothingInfoRepository.findByClothingType_TypeId(typeId);
    }

    // 의류 이름으로 의류 상세 정보 조회
    public List<DetailedClothingInfo> getDetailedClothingInfosByDetailName(String detailName) {
        return detailedClothingInfoRepository.findByDetailNameContaining(detailName);
    }

    // 의류 상세 정보 저장
    public DetailedClothingInfo saveDetailedClothingInfo(DetailedClothingInfo detailedClothingInfo) {
        return detailedClothingInfoRepository.save(detailedClothingInfo);
    }

    // 의류 상세 정보 삭제
    public void deleteDetailedClothingInfo(Long detailId) {
        detailedClothingInfoRepository.deleteById(detailId);
    }
}
