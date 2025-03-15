package com.first.demo.controller;

import com.first.demo.domain.CollectionBin;
import com.first.demo.service.CollectionBinService;
import com.first.demo.service.CSVImporter;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/collection-bins")
public class CollectionBinController {

    private final CollectionBinService collectionBinService;
    private final CSVImporter csvImporter;

    @Autowired
    public CollectionBinController(CollectionBinService collectionBinService, CSVImporter csvImporter) {
        this.collectionBinService = collectionBinService;
        this.csvImporter = csvImporter;
    }

    // 모든 수거함 조회
    @GetMapping
    public List<CollectionBin> getAllCollectionBins() {
        return collectionBinService.getAllCollectionBins();
    }

    // 수거함 ID로 조회 -> Optional을 직접 처리하도록 변경(데이터 없으면 예외 발생)
    @GetMapping("/{binId}")
    public ResponseEntity<?> getCollectionBinById(@PathVariable Long binId) {
        try {
            CollectionBin collectionBin = collectionBinService.getCollectionBinById(binId);
            return ResponseEntity.ok(collectionBin);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
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

    // CSV 파일을 업로드하여 수거함 데이터를 DB에 삽입하는 메서드
    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 비어있습니다.");
        }

        try {
            // CSVImporter 서비스의 importCSV 메서드 호출
            csvImporter.importCSV(file);
            return ResponseEntity.ok("CSV 파일이 성공적으로 업로드되었습니다.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("CSV 파일 처리 중 오류 발생: " + e.getMessage());
        }
    }
}
