package com.ticketingsystem.service;


import com.ticketingsystem.dto.MessageCreateRequest;
import com.ticketingsystem.dto.TicketCreateRequest;
import com.ticketingsystem.entities.Message;
import com.ticketingsystem.entities.Ticket;
import com.ticketingsystem.entities.User;
import com.ticketingsystem.repository.MessageRepository;
import com.ticketingsystem.repository.TicketRepository;
import com.ticketingsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    @Autowired private TicketRepository ticketRepo;
    @Autowired private MessageRepository messageRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private BCryptPasswordEncoder encoder;

    @Override
    public Ticket createTicket(TicketCreateRequest req){
        User requester = userRepo.findByEmail(req.requesterEmail)
                .orElseGet(() -> {
                    User u = new User();
                    u.setFirstName(req.requesterName);
                    u.setEmail(req.requesterEmail);
                    u.setPasswordHash(encoder.encode("temp123"));
                    u.setRole("USER");
                    return userRepo.save(u);
                });
        Ticket t = new Ticket();
        t.setSubject(req.subject);
        t.setDescription(req.description);
        t.setRequester(requester);

        t = ticketRepo.save(t);

        Message m = new Message();
        m.setTicket(t);
        m.setAuthor(requester);
        m.setBody(req.description);
        messageRepo.save(m);

        return t;
    }

    @Override
    public Page<Ticket> listTickets(Pageable pageable) {
        return ticketRepo.findAll(pageable);
    }

    @Override
    public Ticket getTicket(Long id) {
        Ticket t = ticketRepo.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found"));
        t.setMessages(messageRepo.findByTicketIdOrderByCreatedAtAsc(id));
        return t;
    }

    @Override
    public Message addMessage(Long ticketId, MessageCreateRequest dto) {
        Ticket t = ticketRepo.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket not found"));
        User author = null;
        if (dto.authorId != null) {
            author = userRepo.findById(dto.authorId).orElse(null);
        }

        Message m = new Message();
        m.setTicket(t);
        m.setAuthor(author);
        m.setBody(dto.body);
        m.setInternal(dto.internal);
        return messageRepo.save(m);
    }
}