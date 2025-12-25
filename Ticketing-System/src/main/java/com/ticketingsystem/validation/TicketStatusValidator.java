package com.ticketingsystem.validation;

import com.ticketingsystem.enums.TicketStatus;

public final class TicketStatusValidator {

    private TicketStatusValidator() {}

    public static void validateTransition(TicketStatus current, TicketStatus next) {

        if (current == next) {
            throw new IllegalStateException("Ticket is already in status " + current);
        }

        switch (current) {

            case NEW -> {
                if (next != TicketStatus.IN_PROGRESS) {
                    throw new IllegalStateException(
                            "NEW ticket can only move to IN_PROGRESS"
                    );
                }
            }

            case IN_PROGRESS -> {
                if (next != TicketStatus.RESOLVED) {
                    throw new IllegalStateException(
                            "IN_PROGRESS ticket can only move to RESOLVED"
                    );
                }
            }

            case RESOLVED -> {
                if (next != TicketStatus.CLOSED) {
                    throw new IllegalStateException(
                            "RESOLVED ticket can only move to CLOSED"
                    );
                }
            }

            case CLOSED -> {
                throw new IllegalStateException(
                        "CLOSED ticket cannot change status"
                );
            }
        }
    }

}