package com.ticketingsystem.mapper;

import com.ticketingsystem.dto.UserResponse;
import com.ticketingsystem.entities.User;

public final class UserMapper {

    private UserMapper() {}

    public static UserResponse toResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.id = user.getId();
        dto.firstName = user.getFirstName();
        dto.email = user.getEmail();
        dto.role = user.getRole().name();
        return dto;
    }
}