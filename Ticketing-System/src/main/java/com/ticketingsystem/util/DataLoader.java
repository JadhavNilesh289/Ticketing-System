package com.ticketingsystem.util;

import com.ticketingsystem.entities.User;
import com.ticketingsystem.enums.Role;
import com.ticketingsystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
//@Profile("local")
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;

    public DataLoader(UserRepository userRepo,
                      BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {

        if (userRepo.count() > 0) return;

        create("Nilesh", "Jadhav", Role.ADMIN, "admin@example.com", "admin123");
        create("shivansh", "Vish", Role.AGENT, "agent@example.com", "agent123");
        create("demo", "user", Role.USER, "user@example.com", "user123");
    }

    private void create(String firstName, String lastName, Role role, String email, String pass) {
        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(pass));
        u.setRole(role);
        userRepo.save(u);
    }
}