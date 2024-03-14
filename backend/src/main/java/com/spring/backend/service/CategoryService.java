package com.spring.backend.service;

import com.spring.backend.entity.CategoryEntity;
import com.spring.backend.entity.UserEntity;
import com.spring.backend.exception.BusinessException;
import com.spring.backend.payload.request.CategoryRequest;
import com.spring.backend.payload.response.CategoryResponse;
import com.spring.backend.repository.CategoryRepository;
import com.spring.backend.repository.UserRepository;
import com.spring.backend.utilities.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<CategoryResponse> getAllCategories() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAllByUserId(Util.getUserAuthenticated().getId());
        return categoryEntities.stream().map(this::categoryEntityToCategoryResponse).toList();
    }

    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        CategoryEntity categoryEntity = categoryRequestToCategoryEntity(categoryRequest);
        CategoryEntity categoryEntitySaved = categoryRepository.save(categoryEntity);
        return categoryEntityToCategoryResponse(categoryEntitySaved);
    }

    public CategoryResponse categoryEntityToCategoryResponse(CategoryEntity categoryEntity) {
        return CategoryResponse
                .builder()
                .id(categoryEntity.getId())
                .categoryName(categoryEntity.getCategoryName())
                .build();
    }

    public CategoryEntity categoryRequestToCategoryEntity(CategoryRequest categoryRequest) {
        return CategoryEntity
                .builder()
                .categoryName(categoryRequest.getCategoryName())
                .user(userRepository.findById(Util.getUserAuthenticated().getId()).orElseThrow(RuntimeException::new))
                .build();
    }

    public void deleteCategory(Long idCategory) {
        CategoryEntity categoryEntity = categoryRepository.findById(idCategory)
                .orElseThrow(RuntimeException::new);
        Optional<UserEntity> userAuthenticated = userRepository.findById(Util.getUserAuthenticated().getId());

        if (userAuthenticated.isPresent() && !Objects.equals(categoryEntity.getUser().getUsername(), userAuthenticated.get().getUsername())) {
            throw new BusinessException("Accion no permitida");
        }
        categoryRepository.delete(categoryEntity);
    }

    public CategoryResponse updateCategory(CategoryRequest categoryRequest, Long idCategory) {
        CategoryEntity categoryEntity = categoryRepository.findById(idCategory)
                .orElseThrow(RuntimeException::new);
        Optional<UserEntity> userAuthenticated = userRepository.findById(Util.getUserAuthenticated().getId());
        if (userAuthenticated.isPresent() && !Objects.equals(categoryEntity.getUser().getUsername(), userAuthenticated.get().getUsername())) {
            throw new BusinessException("Accion no permitida");
        }
        if (!Objects.isNull(categoryRequest.getCategoryName()) && !Objects.equals(categoryEntity.getCategoryName(),
                categoryRequest.getCategoryName())) {
            categoryEntity.setCategoryName(categoryRequest.getCategoryName());
        }
        CategoryEntity categoryEntitySaved = categoryRepository.save(categoryEntity);
        return categoryEntityToCategoryResponse(categoryEntitySaved);
    }
}
