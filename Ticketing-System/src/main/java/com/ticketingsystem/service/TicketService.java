package com.ticketingsystem.service;

import com.ticketingsystem.dto.MessageCreateDto;
import com.ticketingsystem.dto.TicketCreateDto;
import com.ticketingsystem.entities.Message;
import com.ticketingsystem.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketService {
    Ticket createTicket(TicketCreateDto dto);
    Page<Ticket> listTickets(Pageable pageable);
    Ticket getTicket(Long id);
    Message addMessage(Long ticketId, MessageCreateDto dto);
}