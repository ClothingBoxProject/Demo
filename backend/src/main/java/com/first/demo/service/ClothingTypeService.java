package com.first.demo.service;

import com.first.demo.domain.ClothingType;
import com.first.demo.repository.ClothingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClothingTypeService {

    private final ClothingTypeRepository clothingTypeRepository;

    @Autowired
    public ClothingTypeService(ClothingTypeRepository clothingTypeRepository) {
        this.clothingTypeRepository = clothingTypeRepository;
    }

    // 모든 의류 카테고리 조회
    public List<ClothingType> getAllClothingTypes() {
        return clothingTypeRepository.findAll();
    }

    // 의류 카테고리 ID로 조회
    public Optional<ClothingType> getClothingTypeById(Long typeId) {
        return clothingTypeRepository.findById(typeId);
    }

    // typeName으로 의류 카테고리 조회
    public Optional<ClothingType> getClothingTypeByTypeName(String typeName) {
        return clothingTypeRepository.findByTypeName(typeName);
    }

    // 의류 카테고리 저장
    public ClothingType saveClothingType(ClothingType clothingType) {
        return clothingTypeRepository.save(clothingType);
    }

    // 의류 카테고리 삭제
    public void deleteClothingType(Long typeId) {
        clothingTypeRepository.deleteById(typeId);
    }
}
