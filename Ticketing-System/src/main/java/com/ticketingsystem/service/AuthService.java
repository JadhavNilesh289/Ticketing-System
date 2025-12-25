package com.ticketingsystem.service;

import com.ticketingsystem.entities.User;
import com.ticketingsystem.repository.UserRepository;
import com.ticketingsystem.security.JwtUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(
            UserRepository userRepo,
            BCryptPasswordEncoder encoder,
            JwtUtil jwtUtil
    ) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    // Login
    public String login(String email, String password) {

        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password required");
        }

        String normalizedEmail = email.trim().toLowerCase();

        User user = userRepo.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new SecurityException("Invalid credentials")
                );

        if (!encoder.matches(password, user.getPasswordHash())) {
            throw new SecurityException("Invalid credentials");
        }

        return jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );
    }

    // Current auth user
    public User currentUser() {

        var authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Unauthenticated request");
        }

        String email = authentication.getName();

        return userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new SecurityException("Authenticated user not found"));
    }
}