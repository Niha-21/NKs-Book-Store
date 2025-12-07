package com.nksbookstore.book.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nksbookstore.book.entity.Book;
import com.nksbookstore.book.model.BookDTO;
import com.nksbookstore.book.repository.BooksRepository;
import com.nksbookstore.book.service.BookService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BooksRepository booksRepository;

    @Override
    public List<BookDTO> getBooks() {

        return booksRepository.findAll()
                .stream()
                .map(book -> this.convertEntityToDTO(book))
                .toList();

    }

    @Override
    public BookDTO getBookById(Long id) {
       
        Book book = booksRepository.findById(id)    
                    .orElseThrow(() -> new RuntimeException("Book Not Found"));

        return convertEntityToDTO(book);

    }
    
    private BookDTO convertEntityToDTO(Book book) {

        return new BookDTO(
            book.getId(),
            book.getTitle(),
            book.getDescription(),
            book.getImageUrl(),
            book.getPrice()
        );

    }

}
