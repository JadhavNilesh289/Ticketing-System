package com.ticketingsystem.dto;

import java.util.List;

public class TicketResponse {
    public Long id;
    public String subject;
    public String description;
    public String status;
    public String priority;
    public String createAt;
    public List<MessageResponse> messages;
}
