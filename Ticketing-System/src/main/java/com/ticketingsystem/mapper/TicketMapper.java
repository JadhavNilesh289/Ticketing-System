package com.ticketingsystem.mapper;

import com.ticketingsystem.dto.MessageResponse;
import com.ticketingsystem.dto.TicketResponse;
import com.ticketingsystem.dto.UserResponse;
import com.ticketingsystem.entities.Message;
import com.ticketingsystem.entities.Ticket;
import com.ticketingsystem.entities.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class TicketMapper {

    private TicketMapper() {}

    public static TicketResponse toResponse(Ticket ticket, List<Message> messages) {

        TicketResponse dto = new TicketResponse();
        dto.id = ticket.getId();
        dto.subject = ticket.getSubject();
        dto.description = ticket.getDescription();

        dto.status = ticket.getStatus().name();
        dto.priority = ticket.getPriority().name();

        dto.createdAt = ticket.getCreatedAt();
        dto.updatedAt = ticket.getUpdatedAt();

        dto.requester = toUserResponse(ticket.getRequester());
        dto.assignee = ticket.getAssignee() != null
                ? toUserResponse(ticket.getAssignee())
                : null;

        dto.messages = messages == null
                ? Collections.emptyList()
                : messages.stream()
                .map(TicketMapper::toMessageResponse)
                .collect(Collectors.toList());

        return dto;
    }

    public static MessageResponse toMessageResponse(Message message) {

        MessageResponse dto = new MessageResponse();
        dto.id = message.getId();
        dto.body = message.getBody();
        dto.internal = message.isInternal();
        dto.createdAt = message.getCreatedAt();

        dto.author = message.getAuthor() != null
                ? toUserResponse(message.getAuthor())
                : null;

        return dto;
    }

    public static UserResponse toUserResponse(User user) {

        UserResponse dto = new UserResponse();
        dto.id = user.getId();
        dto.firstName = user.getFirstName();
        dto.lastName = user.getLastName();
        dto.email = user.getEmail();
        dto.role = user.getRole().name();

        return dto;
    }
}