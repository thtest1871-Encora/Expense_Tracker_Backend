package com.authservice.demo.controller;

import com.authservice.demo.dto.*;
import com.authservice.demo.model.UserCredential;
import com.authservice.demo.security.JwtUtil;
import com.authservice.demo.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthService authService;

    public AuthController(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse<Map<String, Object>> register(@RequestBody @jakarta.validation.Valid RegisterRequest req) {
        UserCredential user = authService.register(req);
        return ApiResponse.success("User registered successfully", Map.of("userId", user.getId()));
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(@RequestBody LoginRequest req) {
        UserCredential user = authService.login(req);
        String token = jwtUtil.generateToken(user.getId());
        return ApiResponse.success("Login successful", Map.of("token", token));
    }

    @GetMapping("/validate")
    public ApiResponse<Boolean> validate(@RequestParam String token) {
        boolean isValid = jwtUtil.validate(token);
        return ApiResponse.success("Token validation result", isValid);
    }
}
