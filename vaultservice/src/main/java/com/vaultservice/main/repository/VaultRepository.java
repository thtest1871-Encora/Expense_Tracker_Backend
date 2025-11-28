package com.vaultservice.main.repository;

import com.vaultservice.main.model.VaultFile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VaultRepository extends JpaRepository<VaultFile, Long> {

    List<VaultFile> findByUserIdOrderByCreatedAtDesc(Long userId);
}
