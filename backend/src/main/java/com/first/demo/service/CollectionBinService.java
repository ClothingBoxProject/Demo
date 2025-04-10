package com.first.demo.service;

import com.first.demo.dao.CollectionBin;
import com.first.demo.repository.CollectionBinRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionBinService {

    private final CollectionBinRepository collectionBinRepository;

    @Autowired
    public CollectionBinService(CollectionBinRepository collectionBinRepository) {
        this.collectionBinRepository = collectionBinRepository;
    }

    // 모든 수거함 조회
    public List<CollectionBin> getAllCollectionBins() {
        return collectionBinRepository.findAll();
    }

    // 수거함 ID로 조회
    public CollectionBin getCollectionBinById(Long binId) {
        return collectionBinRepository.findById(binId)
                .orElseThrow(() -> new EntityNotFoundException("Collection bin not found with id: " + binId));
    }


    // 위치 이름으로 수거함을 조회하는 메서드
    public List<CollectionBin> getCollectionBinsByLocationName(String locationName) {
        return collectionBinRepository.findByLocationName(locationName);
    }

    // 주소로 수거함을 조회하는 메서드
    public List<CollectionBin> getCollectionBinsByAddress(String address) {
        return collectionBinRepository.findByAddress(address);
    }

    // 위도와 경도로 수거함을 조회하는 메서드
    public List<CollectionBin> getCollectionBinsByLatitudeAndLongitude(double latitude, double longitude) {
        return collectionBinRepository.findByLatitudeAndLongitude(latitude, longitude);
    }

    // 수거함 생성
    public CollectionBin createCollectionBin(CollectionBin collectionBin) {
        return collectionBinRepository.save(collectionBin);
    }

    // 수거함 삭제
    public void deleteCollectionBin(Long binId) {
        collectionBinRepository.deleteById(binId);
    }
}