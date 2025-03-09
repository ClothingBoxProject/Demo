package com.first.demo.repository;

import com.first.demo.domain.CollectionBin;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionBinRepository extends JpaRepository<CollectionBin, Long> {
    
    // 위치 이름으로 수거함을 조회하는 메서드
    List<CollectionBin> findByLocationName(String locationName);
    
    // 주소로 수거함을 조회하는 메서드
    List<CollectionBin> findByAddress(String address);
    
    // 위도와 경도로 수거함을 조회하는 메서드
    List<CollectionBin> findByLatitudeAndLongitude(double latitude, double longitude);
}
