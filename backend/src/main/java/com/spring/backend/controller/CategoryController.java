package com.spring.backend.controller;

import com.spring.backend.payload.request.CategoryRequest;
import com.spring.backend.payload.response.CategoryResponse;
import com.spring.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        return new ResponseEntity<>(categoryService.createCategory(categoryRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{idCategory}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long idCategory) {
        categoryService.deleteCategory(idCategory);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idCategory}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody @Valid CategoryRequest categoryRequest,
                                                           @PathVariable Long idCategory) {
        return new ResponseEntity<>(categoryService.updateCategory(categoryRequest, idCategory), HttpStatus.OK);
    }
}
