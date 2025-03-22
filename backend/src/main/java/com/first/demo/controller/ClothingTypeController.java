package com.first.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.first.demo.dao.ClothingType;
import com.first.demo.service.ClothingTypeService;

@RestController
@RequestMapping("/api/clothing-types")
public class ClothingTypeController {

    private final ClothingTypeService clothingTypeService;

    @Autowired
    public ClothingTypeController(ClothingTypeService clothingTypeService) {
        this.clothingTypeService = clothingTypeService;
    }

    // 모든 의류 카테고리 조회
    @GetMapping
    public List<ClothingType> getAllClothingTypes() {
        return clothingTypeService.getAllClothingTypes();
    }

    // 특정 의류 카테고리 조회 (typeId로)
    @GetMapping("/{typeId}")
    public ResponseEntity<ClothingType> getClothingTypeById(@PathVariable Long typeId) {
        Optional<ClothingType> clothingType = clothingTypeService.getClothingTypeById(typeId);
        return clothingType.map(ResponseEntity::ok)
                           .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 특정 의류 카테고리 조회 (typeName으로)
    @GetMapping("/name/{typeName}")
    public ResponseEntity<ClothingType> getClothingTypeByTypeName(@PathVariable String typeName) {
        Optional<ClothingType> clothingType = clothingTypeService.getClothingTypeByTypeName(typeName);
        return clothingType.map(ResponseEntity::ok)
                           .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 의류 카테고리 저장
    @PostMapping
    public ResponseEntity<ClothingType> createClothingType(@RequestBody ClothingType clothingType) {
        ClothingType savedClothingType = clothingTypeService.saveClothingType(clothingType);
        return ResponseEntity.ok(savedClothingType);
    }

    // 의류 카테고리 삭제 (typeId로)
    @DeleteMapping("/{typeId}")
    public ResponseEntity<Void> deleteClothingType(@PathVariable Long typeId) {
        clothingTypeService.deleteClothingType(typeId);
        return ResponseEntity.noContent().build();
    }
}
