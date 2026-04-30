package com.pos.pos_system_backend.service;

import com.pos.pos_system_backend.config.JwtUtil;
import com.pos.pos_system_backend.dto.LoginRequest;
import com.pos.pos_system_backend.entity.User;
import com.pos.pos_system_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repo;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository repo,  JwtUtil jwtUtil) {

        this.repo = repo;
        this.jwtUtil = jwtUtil;
    }

    public String login(LoginRequest request) {
        User user = repo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getUsername(), user.getRole());
    }
}
