package com.ticketingsystem.service;

import com.ticketingsystem.dto.MessageCreateRequest;
import com.ticketingsystem.dto.TicketCreateRequest;
import com.ticketingsystem.entities.Message;
import com.ticketingsystem.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketService {
    Ticket createTicket(TicketCreateRequest dto);
    Page<Ticket> listTickets(Pageable pageable);
    Ticket getTicket(Long id);
    Message addMessage(Long ticketId, MessageCreateRequest dto);
}