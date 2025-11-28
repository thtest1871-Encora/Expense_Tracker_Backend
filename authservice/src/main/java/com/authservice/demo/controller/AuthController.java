package com.authservice.demo.controller;

import com.authservice.demo.dto.*;
import com.authservice.demo.security.JwtUtil;
import com.authservice.demo.client.UserClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserClient userClient;

    public AuthController(JwtUtil jwtUtil, UserClient userClient) {
        this.jwtUtil = jwtUtil;
        this.userClient = userClient;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest req) {
        UserResponse saved = userClient.register(req);
        return "User registered: " + saved.getId();
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req) {
        UserResponse user = userClient.validateLogin(req);
        if (user == null) return "Invalid credentials!";
        return jwtUtil.generateToken(user.getId());
    }

    @GetMapping("/validate")
    public boolean validate(@RequestParam String token) {
        return jwtUtil.validate(token);
    }
}
