package com.example.expense.service;

import org.springframework.stereotype.Service;

import com.example.expense.dto.UpdateUserProfileRequest;
import com.example.expense.dto.UserProfileResponse;
import com.example.expense.exception.UserNotFoundException;
import com.example.expense.model.UserProfile;
import com.example.expense.repository.UserProfileRepository;

@Service
public class UserProfileService {

    private final UserProfileRepository repo;

    public UserProfileService(UserProfileRepository repo) {
        this.repo = repo;
    }

    public UserProfileResponse getByUserId(Long userId) {
        UserProfile profile = repo.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Profile not found for userId: " + userId));
        return toResponse(profile);
    }

    public UserProfileResponse upsert(Long userId, UpdateUserProfileRequest req) {
        UserProfile profile = repo.findByUserId(userId).orElse(
                new UserProfile(userId, req.getFullName(), req.getPhone(), req.getAvatarUrl())
        );

        if (req.getFullName() != null) profile.setFullName(req.getFullName());
        if (req.getPhone() != null) profile.setPhone(req.getPhone());
        if (req.getAvatarUrl() != null) profile.setAvatarUrl(req.getAvatarUrl());

        repo.save(profile);
        return toResponse(profile);
    }
    
    public UserProfileResponse createProfile(Long userId, String fullName) {

        return repo.findByUserId(userId)
                .map(existing -> {
                    // If exists, maybe update name? Or just return.
                    // For safety/idempotency, let's just return existing.
                    return toResponse(existing);
                })
                .orElseGet(() -> {
                    UserProfile profile = new UserProfile(userId, fullName, null, null);
                    repo.save(profile);
                    return toResponse(profile);
                });
    }
    
    public void deleteByUserId(Long userId) {
        UserProfile profile = repo.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Profile not found"));
        repo.delete(profile);
    }

    private UserProfileResponse toResponse(UserProfile p) {
        UserProfileResponse r = new UserProfileResponse();
        r.setUserId(p.getUserId());
        r.setFullName(p.getFullName());
        r.setPhone(p.getPhone());
        r.setAvatarUrl(p.getAvatarUrl());
        r.setCreatedAt(p.getCreatedAt());
        r.setUpdatedAt(p.getUpdatedAt());
        return r;
    }
}
