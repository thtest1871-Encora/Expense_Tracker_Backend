package com.categoryservice.main.service;

import com.categoryservice.main.dto.CategoryResponse;
import com.categoryservice.main.dto.CreateCategoryRequest;
import com.categoryservice.main.dto.UpdateCategoryRequest;
import com.categoryservice.main.exception.NotFoundException;
import com.categoryservice.main.exception.ValidationException;
import com.categoryservice.main.model.Category;
import com.categoryservice.main.repository.CategoryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository repo;
    private final DefaultCategoryInitializer defaultCategoryInitializer;

    public CategoryService(CategoryRepository repo,
                           DefaultCategoryInitializer defaultCategoryInitializer) {
        this.repo = repo;
        this.defaultCategoryInitializer = defaultCategoryInitializer;
    }

    // -----------------------------------------------------------
    // CREATE CATEGORY
    // -----------------------------------------------------------
    @Transactional
    public CategoryResponse createCategory(Long userId, CreateCategoryRequest req) {

        if (userId == null) throw new ValidationException("User ID is required");

        // BEFORE CREATING — ensure defaults exist
        defaultCategoryInitializer.createDefaultsIfMissing(userId);

        if (req.getName() == null || req.getName().trim().isEmpty()) {
            throw new ValidationException("Category name is required");
        }

        Category c = new Category();
        c.setUserId(userId);
        c.setName(req.getName().trim());
        c.setType(req.getType().toUpperCase());
        c.setEmoji(req.getEmoji());
        c.setCreatedAt(Instant.now());

        Category saved = repo.save(c);

        return new CategoryResponse(saved.getId(), saved.getName(), saved.getType(), saved.getEmoji());
    }

    // -----------------------------------------------------------
    // GET ALL
    // -----------------------------------------------------------
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll(Long userId) {

        if (userId == null) throw new ValidationException("User ID is required");

        // 🚫 DO NOT create defaults here
        // This is a read-only transaction

        return repo.findByUserId(userId)
                .stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName(), c.getType(), c.getEmoji()))
                .collect(Collectors.toList());
    }

    // -----------------------------------------------------------
    // UPDATE
    // -----------------------------------------------------------
    @Transactional
    public CategoryResponse updateCategory(Long id, Long userId, UpdateCategoryRequest req) {

        if (userId == null) throw new ValidationException("User ID is required");

        Category existing = repo.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (req.getName() == null || req.getName().trim().isEmpty()) {
            throw new ValidationException("Category name is required");
        }

        existing.setName(req.getName().trim());
        existing.setEmoji(req.getEmoji());
        existing.setUpdatedAt(Instant.now());

        Category saved = repo.save(existing);

        return new CategoryResponse(saved.getId(), saved.getName(), saved.getType(), saved.getEmoji());
    }

    // -----------------------------------------------------------
    // DELETE
    // -----------------------------------------------------------
    @Transactional
    public void deleteCategory(Long id, Long userId) {

        if (userId == null) throw new ValidationException("User ID is required");

        Category existing = repo.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        repo.delete(existing);
    }
}
