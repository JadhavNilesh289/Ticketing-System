package com.ticketingsystem.sla;

import com.ticketingsystem.enums.TicketPriority;
import jakarta.persistence.Transient;

import java.time.LocalDateTime;

public class SlaPolicy {

    @Transient
    public boolean isSlaBreached(){
        return priority == TicketPriority.HIGH &&
                createdAt.plusHours(24).isBefore(LocalDateTime.now());
    }
}
