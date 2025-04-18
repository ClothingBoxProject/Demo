package com.first.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.first.demo.dao.ClothingType;

import java.util.Optional;

@Repository
public interface ClothingTypeRepository extends JpaRepository<ClothingType, Long> {

    // typeName으로 의류 카테고리 조회
    Optional<ClothingType> findByTypeName(String typeName);
}
