package com.bookshop.bookstore;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.bookshop.bookstore.model.User;
import com.bookshop.bookstore.repository.UserRepository;

@SpringBootApplication
public class BookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Bean
    CommandLineRunner createAdmin(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByUsername("admin2").isEmpty()) {
                User admin = User.builder()
                .username("admin2")
                .password(encoder.encode("admin123")) 
                .role("ADMIN")
                .shippingAddress("TU Dublin HQ")
                .paymentMethod("Visa **** 1111")
                .build();
                repo.save(admin);
                System.out.println(" Admin user created.");
            }
        };
    }
}
