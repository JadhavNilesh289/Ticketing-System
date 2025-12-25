package com.ticketingsystem.repository;

import com.ticketingsystem.entities.Ticket;
import com.ticketingsystem.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Page<Ticket> findByRequester(User requester, Pageable pageable);
}