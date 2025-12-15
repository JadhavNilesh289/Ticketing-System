package com.ticketingsystem.service;


import com.ticketingsystem.dto.MessageCreateDto;
import com.ticketingsystem.dto.TicketCreateDto;
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
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired private TicketRepository ticketRepo;
    @Autowired private MessageRepository messageRepo;
    @Autowired private UserRepository userRepo;

    @Override
    @Transactional
    public Ticket createTicket(TicketCreateDto dto){
        // find or create requester
        User requester = userRepo.findByEmail(dto.requesterEmail)
                .orElseGet(() -> {
                    User u = new User();
                    u.setFirstName(dto.requesterName);
                    u.setEmail(dto.requesterEmail);
                    u.setPasswordHash("dev");
                    u.setRole("USER");
                    return userRepo.save(u);
                });
        Ticket t = new Ticket();
        t.setSubject(dto.subject);
        t.setDescription(dto.description);
        t.setRequester(requester);
        t.setStatus("NEW");
        t = ticketRepo.save(t);

        Message m = new Message();
        m.setTicket(t);
        m.setAuthor(requester);
        m.setBody(dto.description);
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
        t.setMessages(messageRepo.findByTicketOrderByCreatedAtAsc(id));
        return t;
    }

    @Override
    public Message addMessage(Long ticketId, MessageCreateDto dto) {
        Ticket t = ticketRepo.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket not found"));
        User author = null;
        if (dto.authorId != null) {
            author = userRepo.findById(dto.authorId).orElse(null);
        }

        Message m = new Message();
        m.setTicket(t);
        m.setAuthor(author);
        m.setBody(dto.body);
        m.setIsInternal(dto.isInternal);
        return messageRepo.save(m);
    }
}