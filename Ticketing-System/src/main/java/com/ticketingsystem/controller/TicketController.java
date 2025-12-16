package com.ticketingsystem.controller;

import com.ticketingsystem.dto.MessageCreateRequest;
import com.ticketingsystem.dto.MessageResponse;
import com.ticketingsystem.dto.TicketCreateRequest;
import com.ticketingsystem.dto.TicketResponse;
import com.ticketingsystem.mapper.TicketMapper;
import com.ticketingsystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    //Create Tickets
    @PostMapping
    public ResponseEntity<TicketResponse> create(
            @RequestBody TicketCreateRequest dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TicketMapper.toResponse(
                        ticketService.createTicket(dto)));
    }

    //List Tickets
    @GetMapping
    public Page<TicketResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ticketService
                .listTickets(PageRequest.of(page, size))
                .map(TicketMapper::toResponse);
    }

    //Get Single Ticket
    @GetMapping("/{id}")
    public TicketResponse get(@PathVariable Long id) {

        return TicketMapper.toResponse(
                ticketService.getTicket(id));
    }

    // Add Message to Ticket
    @PostMapping("/{id}/messages")
    public ResponseEntity<MessageResponse> addMessage(
            @PathVariable Long id,
            @RequestBody MessageCreateRequest dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TicketMapper.toMessageResponse(
                        ticketService.addMessage(id, dto)));
    }
}
