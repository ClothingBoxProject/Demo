package com.first.demo.controller;

import com.first.demo.dao.DetailedClothingInfo;
import com.first.demo.service.DetailedClothingInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/detailed-clothing-info")
public class DetailedClothingInfoController {

    private final DetailedClothingInfoService detailedClothingInfoService;

    @Autowired
    public DetailedClothingInfoController(DetailedClothingInfoService detailedClothingInfoService) {
        this.detailedClothingInfoService = detailedClothingInfoService;
    }

    // 모든 의류 상세 정보 조회
    @GetMapping
    public List<DetailedClothingInfo> getAllDetailedClothingInfos() {
        return detailedClothingInfoService.getAllDetailedClothingInfos();
    }

    // 특정 의류 상세 정보 조회 (ID로)
    @GetMapping("/{detailId}")
    public ResponseEntity<DetailedClothingInfo> getDetailedClothingInfoById(@PathVariable Long detailId) {
        Optional<DetailedClothingInfo> detailedClothingInfo = detailedClothingInfoService.getDetailedClothingInfoById(detailId);
        return detailedClothingInfo.map(ResponseEntity::ok)
                                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 의류 카테고리 ID로 의류 상세 정보 조회
    @GetMapping("/type/{typeId}")
    public List<DetailedClothingInfo> getDetailedClothingInfosByTypeId(@PathVariable Long typeId) {
        return detailedClothingInfoService.getDetailedClothingInfosByTypeId(typeId);
    }

    // 의류 이름으로 의류 상세 정보 조회
    @GetMapping("/name/{detailName}")
    public List<DetailedClothingInfo> getDetailedClothingInfosByDetailName(@PathVariable String detailName) {
        return detailedClothingInfoService.getDetailedClothingInfosByDetailName(detailName);
    }

    // 의류 상세 정보 저장
    @PostMapping
    public ResponseEntity<DetailedClothingInfo> createDetailedClothingInfo(@RequestBody DetailedClothingInfo detailedClothingInfo) {
        DetailedClothingInfo savedDetailedClothingInfo = detailedClothingInfoService.saveDetailedClothingInfo(detailedClothingInfo);
        return ResponseEntity.ok(savedDetailedClothingInfo);
    }

    // 의류 상세 정보 삭제
    @DeleteMapping("/{detailId}")
    public ResponseEntity<Void> deleteDetailedClothingInfo(@PathVariable Long detailId) {
        detailedClothingInfoService.deleteDetailedClothingInfo(detailId);
        return ResponseEntity.noContent().build();
    }
}
