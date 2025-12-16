package com.ticketingsystem.mapper;

import com.ticketingsystem.dto.MessageResponse;
import com.ticketingsystem.dto.TicketResponse;
import com.ticketingsystem.entities.*;

import java.util.List;
import java.util.stream.Collectors;

public class TicketMapper {

    private TicketMapper(){
    }

    public static TicketResponse toResponse(Ticket ticket) {
        TicketResponse dto = new TicketResponse();
        dto.id = ticket.getId();
        dto.subject = ticket.getSubject();
        dto.description = ticket.getDescription();
        dto.status = ticket.getStatus().name();
        dto.priority = ticket.getPriority().name();
        dto.createAt = ticket.getCreatedAt().toString();

        dto.messages = ticket.getMessages() == null
                ? List.of()
                : ticket.getMessages()
                .stream()
                .map(TicketMapper::toMessageResponse)
                .collect(Collectors.toList());

        return dto;
    }
    
    public static MessageResponse toMessageResponse(Message m) {
        MessageResponse mr = new MessageResponse();
        mr.id = m.getId();
        mr.body = m.getBody();
        mr.internal = m.isInternal();
        mr.createdAt = m.getCreatedAt().toString();
        mr.authorName = m.getAuthor() != null
                ? m.getAuthor().getFirstName()
                : "System";
        return mr;
    }
}
