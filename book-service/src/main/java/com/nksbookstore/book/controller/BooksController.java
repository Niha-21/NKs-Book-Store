package com.nksbookstore.book.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nksbookstore.book.model.BookDTO;
import com.nksbookstore.book.service.BookService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksController {
    
    private final BookService bookService;

    @GetMapping("/listBooks")
    public List<BookDTO> getBooks() {

        return bookService.getBooks();

    }
    
    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable Long id) {
        
        return bookService.getBookById(id);

    }
    

}
