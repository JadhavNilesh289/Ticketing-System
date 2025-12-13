package com.ticketingsystem.repository;

import com.ticketingsystem.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Page<Ticket> finaAll(Pageable pageable);
}
