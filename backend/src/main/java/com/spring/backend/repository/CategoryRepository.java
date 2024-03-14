package com.spring.backend.repository;

import com.spring.backend.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findAllByUserId(Long id);

    Optional<CategoryEntity> findByCategoryName(String categoryName);
}
