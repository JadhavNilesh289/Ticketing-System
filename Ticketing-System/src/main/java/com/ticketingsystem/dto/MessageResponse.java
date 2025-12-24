package com.ticketingsystem.dto;

import java.time.LocalDateTime;

public class MessageResponse {
    public Long id;
    public String body;
    public boolean internal;
    public LocalDateTime createdAt;
    public UserResponse author;
}