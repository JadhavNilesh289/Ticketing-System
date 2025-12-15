package com.ticketingsystem.util;

import com.ticketingsystem.entities.User;
import com.ticketingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired private UserRepository userRepo;

    @Override
    public void run(String... args) {
        if (userRepo.count() == 0) {
            User admin = new User();

            admin.setRole("Admin");
            admin.setEmail("admin@example.com");
            admin.setPasswordHash("password");
            admin.setRole("ADMIN");
            userRepo.save(admin);

            User emp = new User();
            emp.setFirstName("Employee");
            emp.setEmail("employee@example.com");
            emp.setPasswordHash("password");
            emp.setRole("EMPLOYEE");
            userRepo.save(emp);

            User u = new User();
            u.setFirstName("User");
            u.setEmail("user@example.com");
            u.setPasswordHash("password");
            u.setRole("USER");
            userRepo.save(u);
        }
    }
}
