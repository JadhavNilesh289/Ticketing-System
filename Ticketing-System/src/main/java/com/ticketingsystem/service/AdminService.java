package com.ticketingsystem.service;


import com.ticketingsystem.dto.UserResponse;
import com.ticketingsystem.entities.Ticket;
import com.ticketingsystem.entities.User;
import com.ticketingsystem.enums.Role;
import com.ticketingsystem.enums.TicketStatus;
import com.ticketingsystem.mapper.UserMapper;
import com.ticketingsystem.repository.TicketRepository;
import com.ticketingsystem.repository.UserRepository;
import com.ticketingsystem.validation.TicketAssignmentValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepo;
    private final TicketRepository ticketRepo;
    private final BCryptPasswordEncoder encoder;

    public AdminService(
            UserRepository userRepo,
            TicketRepository ticketRepo,
            BCryptPasswordEncoder encoder
    ) {
        this.userRepo = userRepo;
        this.ticketRepo = ticketRepo;
        this.encoder = encoder;
    }

    public UserResponse createUser(
            String firstName,
            String lastName,
            String email,
            String rawPassword,
            Role role
    ) {

        if (userRepo.findByEmail(email).isPresent()) {
            throw new IllegalStateException("User already exists");
        }

        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(rawPassword));
        u.setRole(role);

        User saved = userRepo.save(u);
        return UserMapper.toResponse(saved);
    }

    public Ticket assignTicket(Long ticketId, Long agentId) {

        Ticket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        User agent = userRepo.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        TicketAssignmentValidator.validateAssignment(ticket, agent);

        ticket.setAssignee(agent);
        ticket.setStatus(TicketStatus.IN_PROGRESS);

        return ticketRepo.save(ticket);
    }
}