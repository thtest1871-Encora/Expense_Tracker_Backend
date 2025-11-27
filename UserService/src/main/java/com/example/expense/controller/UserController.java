package com.example.expense.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.expense.dto.CreateUserProfileRequest;
import com.example.expense.dto.UpdateUserProfileRequest;
import com.example.expense.dto.UserProfileResponse;
import com.example.expense.security.JwtUtil;
import com.example.expense.service.UserProfileService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserProfileService service;
    private final JwtUtil jwtUtil;

    public UserController(UserProfileService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    private boolean isOwner(HttpServletRequest request, Long pathUserId) {
        Long tokenUserId = (Long) request.getAttribute("userId");
        return tokenUserId != null && tokenUserId.equals(pathUserId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(
            @PathVariable Long userId,
            HttpServletRequest request) {

        if (!isOwner(request, userId)) {
            return ResponseEntity.status(403).body("Access denied");
        }

        return ResponseEntity.ok(service.getByUserId(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long userId,
            @RequestBody UpdateUserProfileRequest req,
            HttpServletRequest request) {

        if (!isOwner(request, userId)) {
            return ResponseEntity.status(403).body("Access denied");
        }

        return ResponseEntity.ok(service.upsert(userId, req));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteProfile(
            @PathVariable Long userId,
            HttpServletRequest request) {

        if (!isOwner(request, userId)) {
            return ResponseEntity.status(403).body("Access denied");
        }

        service.deleteByUserId(userId);
        return ResponseEntity.ok("Profile deleted");
    }

    // Internal calls
    @PostMapping
    public ResponseEntity<UserProfileResponse> createProfile(
            @RequestBody CreateUserProfileRequest req) {
        return ResponseEntity.ok(service.createProfile(req.getUserId(), req.getFullName()));
    }

    // Public testing endpoint
    @GetMapping("/token/{userId}")
    public String createToken(@PathVariable Long userId) {
        return jwtUtil.generateToken(userId);
    }
}
