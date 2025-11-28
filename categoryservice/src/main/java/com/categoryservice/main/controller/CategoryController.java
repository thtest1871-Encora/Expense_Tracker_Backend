package com.categoryservice.main.controller;

import com.categoryservice.main.dto.*;
import com.categoryservice.main.service.CategoryService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateCategoryRequest req) {

        return ResponseEntity.status(201)
                .body(service.createCategory(userId, req));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll(
            @RequestHeader("X-User-Id") Long userId) {

        return ResponseEntity.ok(service.getAll(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest req) {

        return ResponseEntity.ok(service.updateCategory(id, userId, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id) {

        service.deleteCategory(id, userId);
        return ResponseEntity.noContent().build();
    }
}
