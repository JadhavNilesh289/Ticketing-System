package com.ticketingsystem.controller;

import com.ticketingsystem.dto.MessageCreateDto;
import com.ticketingsystem.dto.TicketCreateDto;
import com.ticketingsystem.entities.Message;
import com.ticketingsystem.entities.Ticket;
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
    @Autowired private TicketService ticketService;

    @PostMapping
    public ResponseEntity<Ticket> create(@RequestBody TicketCreateDto dto) {
        Ticket t = ticketService.createTicket(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(t);
    }

    @GetMapping
    public Page<Ticket> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ticketService.listTickets(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public Ticket get(@PathVariable Long id) {
        return ticketService.getTicket(id);
    }

    @PostMapping("/{id}/messages")
    public ResponseEntity<Message> addMessage(@PathVariable Long id, @RequestBody MessageCreateDto dto) {
        Message m = ticketService.addMessage(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(m);
    }
}
