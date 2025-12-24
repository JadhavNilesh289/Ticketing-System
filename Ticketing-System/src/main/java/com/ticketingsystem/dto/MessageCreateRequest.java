package com.ticketingsystem.dto;

import jakarta.validation.constraints.NotBlank;

public class MessageCreateRequest {

    @NotBlank
    public String body;
    public boolean internal;
}
