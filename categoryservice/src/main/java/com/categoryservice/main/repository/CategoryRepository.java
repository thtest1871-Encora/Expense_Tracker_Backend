package com.categoryservice.main.repository;

import com.categoryservice.main.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserIdOrderByNameAsc(Long userId);

    List<Category> findByUserId(Long userId);

    Optional<Category> findByUserIdAndId(Long userId, Long id);

    // ⭐ Required for default category initializer
    boolean existsByUserIdAndNameIgnoreCase(Long userId, String name);
}
