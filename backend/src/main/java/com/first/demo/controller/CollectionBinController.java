package com.first.demo.controller;

import com.first.demo.domain.CollectionBin;
import com.first.demo.service.CollectionBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/collection-bins")
public class CollectionBinController {

    private final CollectionBinService collectionBinService;

    @Autowired
    public CollectionBinController(CollectionBinService collectionBinService) {
        this.collectionBinService = collectionBinService;
    }

    // 모든 수거함 조회
    @GetMapping
    public List<CollectionBin> getAllCollectionBins() {
        return collectionBinService.getAllCollectionBins();
    }

    // 수거함 ID로 조회
    @GetMapping("/{binId}")
    public ResponseEntity<CollectionBin> getCollectionBinById(@PathVariable Long binId) {
        Optional<CollectionBin> collectionBin = collectionBinService.getCollectionBinById(binId);
        return collectionBin.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 위치 이름으로 수거함 조회
    @GetMapping("/location")
    public List<CollectionBin> getCollectionBinsByLocationName(@RequestParam String locationName) {
        return collectionBinService.getCollectionBinsByLocationName(locationName);
    }

    // 주소로 수거함 조회
    @GetMapping("/address")
    public List<CollectionBin> getCollectionBinsByAddress(@RequestParam String address) {
        return collectionBinService.getCollectionBinsByAddress(address);
    }

    // 위도와 경도로 수거함 조회
    @GetMapping("/coordinates")
    public List<CollectionBin> getCollectionBinsByLatitudeAndLongitude(@RequestParam double latitude, @RequestParam double longitude) {
        return collectionBinService.getCollectionBinsByLatitudeAndLongitude(latitude, longitude);
    }

    // 수거함 생성
    @PostMapping
    public ResponseEntity<CollectionBin> createCollectionBin(@RequestBody CollectionBin collectionBin) {
        CollectionBin createdBin = collectionBinService.createCollectionBin(collectionBin);
        return ResponseEntity.status(201).body(createdBin);
    }

    // 수거함 삭제
    @DeleteMapping("/{binId}")
    public ResponseEntity<Void> deleteCollectionBin(@PathVariable Long binId) {
        collectionBinService.deleteCollectionBin(binId);
        return ResponseEntity.noContent().build();
    }
}
