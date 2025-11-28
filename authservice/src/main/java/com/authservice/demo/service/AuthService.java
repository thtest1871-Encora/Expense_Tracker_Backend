package com.authservice.demo.service;

import com.authservice.demo.dto.LoginRequest;
import com.authservice.demo.dto.RegisterRequest;
import com.authservice.demo.model.UserCredential;
import com.authservice.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public UserCredential register(RegisterRequest req) {

        if (repo.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        UserCredential user = new UserCredential();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        return repo.save(user);
    }

    public UserCredential login(LoginRequest req) {
        return repo.findByEmail(req.getEmail())
                .filter(u -> passwordEncoder.matches(req.getPassword(), u.getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }
}
