package com.ticketingsystem.controller;

import com.ticketingsystem.dto.MessageCreateRequest;
import com.ticketingsystem.dto.TicketCreateRequest;
import com.ticketingsystem.dto.TicketResponse;
import com.ticketingsystem.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> create(
           @Valid @RequestBody TicketCreateRequest request
    ) {
        TicketResponse response = ticketService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public Page<TicketResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ticketService.listTickets(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public TicketResponse get(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean includeInternal
    ) {
        return ticketService.getTicket(id, includeInternal);
    }

    @PostMapping("/{id}/messages")
    public ResponseEntity<Void> addMessage(
            @PathVariable Long id,
            @Valid @RequestBody MessageCreateRequest request
    ) {
        ticketService.addMessage(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}