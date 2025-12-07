package com.nksbookstore.book.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nksbookstore.book.entity.Book;
import com.nksbookstore.book.repository.BooksRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final BooksRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {

        if (bookRepository.count() > 0) {
            return; // Preventing duplicate inserts
        }

        Book b1 = new Book();
        b1.setTitle("Atomic Habits");
        b1.setDescription("A guide to building good habits.");
        b1.setPrice(new BigDecimal("399"));
        b1.setImageUrl("https://example.com/atomic_habits.jpg");

        Book b2 = new Book();
        b2.setTitle("The Alchemist");
        b2.setDescription("A magical story about pursuing dreams.");
        b2.setPrice(new BigDecimal("299"));
        b2.setImageUrl("https://example.com/alchemist.jpg");

        Book b3 = new Book();
        b3.setTitle("Clean Code");
        b3.setDescription("A handbook of agile software craftsmanship.");
        b3.setPrice(new BigDecimal("599"));
        b3.setImageUrl("https://example.com/clean_code.jpg");

        bookRepository.saveAll(List.of(b1, b2, b3));

        System.out.println("Default books loaded successfully!");

    }
}
