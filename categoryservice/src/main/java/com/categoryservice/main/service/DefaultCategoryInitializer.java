package com.categoryservice.main.service;

import com.categoryservice.main.model.Category;
import com.categoryservice.main.repository.CategoryRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Component
public class DefaultCategoryInitializer {

    private final CategoryRepository repo;

    public DefaultCategoryInitializer(CategoryRepository repo) {
        this.repo = repo;
    }

    private static final List<DefaultCategory> DEFAULTS = Arrays.asList(
            new DefaultCategory("Food & Dining", "EXPENSE", "🍔"),
            new DefaultCategory("Transport", "EXPENSE", "🚌"),
            new DefaultCategory("Shopping", "EXPENSE", "🛍️"),
            new DefaultCategory("Salary", "INCOME", "💼"),
            new DefaultCategory("Investments", "INCOME", "📈")
    );

    @Transactional
    public void createDefaultsIfMissing(Long userId) {
        for (DefaultCategory def : DEFAULTS) {
            boolean exists = repo.existsByUserIdAndNameIgnoreCase(userId, def.name());
            if (!exists) {
                Category c = new Category();
                c.setUserId(userId);
                c.setName(def.name());
                c.setType(def.type());
                c.setEmoji(def.emoji());
                c.setCreatedAt(Instant.now());
                repo.save(c);
            }
        }
    }

    private record DefaultCategory(String name, String type, String emoji) {}
}
