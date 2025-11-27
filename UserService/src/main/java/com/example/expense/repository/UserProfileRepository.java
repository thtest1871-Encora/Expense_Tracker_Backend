package com.example.expense.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.expense.model.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
	Optional<UserProfile> findByUserId(Long userId);
}