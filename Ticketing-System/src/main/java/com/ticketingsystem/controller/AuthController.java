package com.ticketingsystem.controller;

import com.ticketingsystem.dto.AuthResponse;
import com.ticketingsystem.dto.LoginRequest;
import com.ticketingsystem.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request
    ) {
        String token = authService.login(
                request.email,
                request.password
        );

        return ResponseEntity.ok(new AuthResponse(token));
    }
}