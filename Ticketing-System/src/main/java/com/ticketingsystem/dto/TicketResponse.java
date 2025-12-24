package com.ticketingsystem.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TicketResponse {

    public Long id;
    public String subject;
    public String description;

    public String status;
    public String priority;

    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public UserResponse requester;
    public UserResponse assignee;

    public List<MessageResponse> messages;
}