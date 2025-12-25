package com.ticketingsystem.service;

import com.ticketingsystem.dto.MessageCreateRequest;
import com.ticketingsystem.dto.TicketCreateRequest;
import com.ticketingsystem.dto.TicketResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketService {

    TicketResponse createTicket(TicketCreateRequest request);

    Page<TicketResponse> listTickets(Pageable pageable);

    TicketResponse getTicket(Long ticketId, boolean includeInternalMessages);

    void addMessage(Long ticketId, MessageCreateRequest request);

    void changeStatus(Long ticketId, String nextStatus);
}