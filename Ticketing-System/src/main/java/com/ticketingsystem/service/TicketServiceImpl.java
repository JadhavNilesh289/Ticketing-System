package com.ticketingsystem.service;

import com.ticketingsystem.dto.MessageCreateRequest;
import com.ticketingsystem.dto.TicketCreateRequest;
import com.ticketingsystem.dto.TicketResponse;
import com.ticketingsystem.entities.Message;
import com.ticketingsystem.entities.Ticket;
import com.ticketingsystem.entities.User;
import com.ticketingsystem.enums.Role;
import com.ticketingsystem.enums.TicketStatus;
import com.ticketingsystem.mapper.TicketMapper;
import com.ticketingsystem.repository.MessageRepository;
import com.ticketingsystem.repository.TicketRepository;
import com.ticketingsystem.repository.UserRepository;
import com.ticketingsystem.validation.TicketAssignmentValidator;
import com.ticketingsystem.validation.TicketStatusValidator;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepo;
    private final MessageRepository messageRepo;
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthService authService;


    public TicketServiceImpl(
            TicketRepository ticketRepo,
            MessageRepository messageRepo,
            UserRepository userRepo,
            BCryptPasswordEncoder passwordEncoder,
            AuthService authService
    ) {
        this.ticketRepo = ticketRepo;
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }


    @Override
    public TicketResponse createTicket(TicketCreateRequest req) {

        User requester = userRepo.findByEmail(req.requesterEmail)
                .orElseGet(() -> createRequester(req));

        Ticket ticket = new Ticket();
        ticket.setSubject(req.subject);
        ticket.setDescription(req.description);
        ticket.setRequester(requester);

        ticketRepo.save(ticket);

        Message firstMessage = new Message();
        firstMessage.setTicket(ticket);
        firstMessage.setAuthor(requester);
        firstMessage.setBody(req.description);
        firstMessage.setInternal(false);
        messageRepo.save(firstMessage);

        return buildTicketResponse(ticket, false);
    }


    @Override
    public Page<TicketResponse> listTickets(Pageable pageable) {
        return ticketRepo.findAll(pageable)
                .map(ticket -> buildTicketResponse(ticket, false));
    }

    @Override
    public TicketResponse getTicket(Long ticketId, boolean includeInternal) {

        Ticket ticket = findTicket(ticketId);
        User viewer = authService.currentUser();

        boolean staff =
                viewer.getRole() == Role.ADMIN ||
                        viewer.getRole() == Role.AGENT;

        return buildTicketResponse(ticket, staff && includeInternal);
    }


    @Override
    public void addMessage(Long ticketId, MessageCreateRequest req) {

        Ticket ticket = findTicket(ticketId);

        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new IllegalStateException("Cannot add message to CLOSED ticket");
        }

        User author = authService.currentUser();

        if (req.internal && author.getRole() == Role.USER) {
            throw new IllegalStateException("USER cannot add internal messages");
        }

        Message message = new Message();
        message.setTicket(ticket);
        message.setAuthor(author);
        message.setBody(req.body);
        message.setInternal(req.internal);

        messageRepo.save(message);
    }

    @Override
    public void assignTicket(Long ticketId, Long agentId) {

        Ticket ticket = findTicket(ticketId);
        User agent = findUser(agentId);

        TicketAssignmentValidator.validateAssignment(ticket, agent);

        ticket.setAssignee(agent);
        ticket.setStatus(TicketStatus.IN_PROGRESS);

        ticketRepo.save(ticket);
    }

    @Override
    public void changeStatus(Long ticketId, String nextStatus) {

        Ticket ticket = findTicket(ticketId);
        TicketStatus next;
        try {
            next = TicketStatus.valueOf(nextStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid ticket status");
        }

        TicketStatusValidator.validateTransition(ticket.getStatus(), next);

        ticket.setStatus(next);
        ticketRepo.save(ticket);
    }

    // helpers

    private User createRequester(TicketCreateRequest req) {
        User u = new User();
        u.setFirstName(req.requesterName);
        u.setEmail(req.requesterEmail);
        u.setPasswordHash(passwordEncoder.encode("TEMP-LOGIN-DISABLED"));
        u.setRole(Role.USER);
        return userRepo.save(u);
    }

    private Ticket findTicket(Long id) {
        return ticketRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    private User findUser(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private TicketResponse buildTicketResponse(Ticket ticket, boolean includeInternal) {

        var messages = messageRepo
                .findByTicketIdOrderByCreatedAtAsc(ticket.getId());

        if (!includeInternal) {
            messages = messages.stream()
                    .filter(m -> !m.isInternal())
                    .toList();
        }

        return TicketMapper.toResponse(ticket, messages);
    }
}