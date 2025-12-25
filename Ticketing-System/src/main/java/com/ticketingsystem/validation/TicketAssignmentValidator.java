package com.ticketingsystem.validation;

import com.ticketingsystem.entities.Ticket;
import com.ticketingsystem.entities.User;
import com.ticketingsystem.enums.Role;
import com.ticketingsystem.enums.TicketStatus;

public final class TicketAssignmentValidator {

    private TicketAssignmentValidator() {}

    public static void validateAssignment(Ticket ticket, User agent) {

        if (agent == null) {
            throw new IllegalArgumentException("Assignee user not found");
        }

        if (agent.getRole() != Role.AGENT) {
            throw new IllegalStateException("USER cannot be assigned to a ticket");
        }

        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new IllegalStateException("CLOSED ticket cannot be assigned");
        }

        if (ticket.getAssignee() != null) {
            throw new IllegalStateException("Ticket is already assigned");
        }

        if (ticket.getStatus() != TicketStatus.NEW) {
            throw new IllegalStateException(
                    "Only NEW tickets can be assigned"
            );
        }
    }
}