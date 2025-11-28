package com.example.expense.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.expense.dto.ApiResponse;
import com.example.expense.dto.CreateUserProfileRequest;
import com.example.expense.dto.UpdateUserProfileRequest;
import com.example.expense.dto.UserProfileResponse;
import com.example.expense.service.UserProfileService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserProfileService service;

    public UserController(UserProfileService service) {
        this.service = service;
    }

    private boolean isOwner(HttpServletRequest request, Long pathUserId) {
        String header = request.getHeader("X-User-Id");
        if (header == null) return false;

        Long authenticatedUserId = Long.valueOf(header);
        return authenticatedUserId.equals(pathUserId);
    }

    @GetMapping("/{userId}")
    public ApiResponse<?> getProfile(
            @PathVariable Long userId,
            HttpServletRequest request) {

        if (!isOwner(request, userId)) {
            return ApiResponse.error("Access denied");
        }

        return ApiResponse.success("Profile retrieved", service.getByUserId(userId));
    }

    @PutMapping("/{userId}")
    public ApiResponse<?> updateProfile(
            @PathVariable Long userId,
            @RequestBody UpdateUserProfileRequest req,
            HttpServletRequest request) {

        if (!isOwner(request, userId)) {
            return ApiResponse.error("Access denied");
        }

        return ApiResponse.success("Profile updated", service.upsert(userId, req));
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<?> deleteProfile(
            @PathVariable Long userId,
            HttpServletRequest request) {

        if (!isOwner(request, userId)) {
            return ApiResponse.error("Access denied");
        }

        service.deleteByUserId(userId);
        return ApiResponse.success("Profile deleted", null);
    }

    // Internal call — ALLOWED via Gateway without session userId
    @PostMapping
    public ApiResponse<UserProfileResponse> createProfile(@RequestBody CreateUserProfileRequest req){
        return ApiResponse.success("Profile created", service.createProfile(req.getUserId(), req.getFullName()));
    }
}
