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
    
    private Long getUserIdFromRequest(HttpServletRequest req) {
        return Long.valueOf(req.getHeader("X-User-Id"));
    }

//    private boolean isOwner(HttpServletRequest request, Long pathUserId) {
//        Long userId =  Long.valueOf(request.getHeader("X-User-Id"));
//        if (userId == null) return false;
//
////        Long authenticatedUserId = Long.valueOf(header);
//        return userId.equals(pathUserId);
//    }

    @GetMapping
    public ApiResponse<?> getProfile(
            HttpServletRequest request) {

//        if (!isOwner(request, userId)) {
//            return ApiResponse.error("Access denied");
//        }

        return ApiResponse.success("Profile retrieved", service.getByUserId(getUserIdFromRequest(request)));
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getProfileById(@PathVariable Long id) {
        return ApiResponse.success("Profile retrieved", service.getByUserId(id));
    }

    @PutMapping
    public ApiResponse<?> updateProfile(
            @RequestBody UpdateUserProfileRequest req,
            HttpServletRequest request) {

//        if (!isOwner(request, userId)) {
//            return ApiResponse.error("Access denied");
//        }

        return ApiResponse.success("Profile updated", service.upsert(getUserIdFromRequest(request), req));
    }

    @DeleteMapping
    public ApiResponse<?> deleteProfile(
            HttpServletRequest request) {

//        if (!isOwner(request, userId)) {
//            return ApiResponse.error("Access denied");
//        }

        service.deleteByUserId(getUserIdFromRequest(request));
        return ApiResponse.success("Profile deleted", null);
    }

    // Internal call — ALLOWED via Gateway without session userId
    @PostMapping
    public ApiResponse<UserProfileResponse> createProfile(@RequestBody CreateUserProfileRequest req){
        return ApiResponse.success("Profile created", service.createProfile(req.getUserId(), req.getFullName(), req.getEmail()));
    }

    @PostMapping("/internal/base-profile")
    public ApiResponse<UserProfileResponse> createBaseProfile(@RequestBody CreateUserProfileRequest req){
        return ApiResponse.success("Profile created", service.createProfile(req.getUserId(), req.getFullName(), req.getEmail()));
    }
}
