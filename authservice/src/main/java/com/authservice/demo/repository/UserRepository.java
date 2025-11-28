package com.authservice.demo.repository;

import com.authservice.demo.model.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserCredential, Long> {
    Optional<UserCredential> findByEmail(String email);
}
