package com.authservice.demo.service;

import com.authservice.demo.dto.LoginRequest;
import com.authservice.demo.dto.RegisterRequest;
import com.authservice.demo.model.UserCredential;
import com.authservice.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    public AuthService(UserRepository repo, PasswordEncoder passwordEncoder, RestTemplate restTemplate) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    public UserCredential register(RegisterRequest req) {

        if (repo.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        UserCredential user = new UserCredential();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        UserCredential savedUser = repo.save(user);

        // Call User Service to create profile
        try {
            Map<String, Object> profileReq = new HashMap<>();
            profileReq.put("userId", savedUser.getId());
            profileReq.put("fullName", savedUser.getName());
            profileReq.put("email", savedUser.getEmail());

            restTemplate.postForObject("http://user-service/users/internal/base-profile", profileReq, Object.class);
        } catch (Exception e) {
            // Rollback: Delete the user if profile creation fails
            repo.delete(savedUser);
            throw new RuntimeException("User profile creation failed. Registration aborted.");
        }

        return savedUser;
    }

    public UserCredential login(LoginRequest req) {
        return repo.findByEmail(req.getEmail())
                .filter(u -> passwordEncoder.matches(req.getPassword(), u.getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }
}
