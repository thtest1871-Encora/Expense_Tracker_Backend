package com.vaultservice.main.repository;

import com.vaultservice.main.model.VaultFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VaultRepository extends JpaRepository<VaultFile, Long> {

    List<VaultFile> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    List<VaultFile> findByUserIdAndDateBetweenOrderByCreatedAtDesc(
            Long userId, LocalDate start, LocalDate end
    );

    List<VaultFile> findByUserIdAndCategoryIdOrderByCreatedAtDesc(Long userId, Long categoryId);

    List<VaultFile> findByUserIdAndCategoryIdAndDateBetweenOrderByCreatedAtDesc(
            Long userId, Long categoryId, LocalDate start, LocalDate end
    );
}
