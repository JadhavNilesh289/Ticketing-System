package com.ticketingsystem.controller;

import com.ticketingsystem.dto.CreateUserRequest;
import com.ticketingsystem.dto.UserResponse;
import com.ticketingsystem.enums.Role;
import com.ticketingsystem.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/agents")
    public ResponseEntity<UserResponse> createAgent(
           @Valid @RequestBody CreateUserRequest request
    ) {
        UserResponse user = adminService.createUser(
                        request.firstName,
                        request.lastName,
                        request.email,
                        request.password,
                        Role.AGENT
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/tickets/{ticketId}/assign/{agentId}")
    public ResponseEntity<Void> assignTicket(
            @PathVariable Long ticketId,
            @PathVariable Long agentId
    ) {
        adminService.assignTicket(ticketId, agentId);
        return ResponseEntity.noContent().build();
    }
}