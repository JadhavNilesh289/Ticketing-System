package com.ticketingsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TicketCreateRequest {

    @NotBlank
    @Size(max = 200)
    public String subject;

    @NotBlank
    @Size(max = 5000)
    public String description;

    @NotBlank
    @Size(max = 100)
    public String requesterName;

    @Email
    @NotBlank
    public String requesterEmail;
}