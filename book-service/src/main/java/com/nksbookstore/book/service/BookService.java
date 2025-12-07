package com.nksbookstore.book.service;

import java.util.List;

import com.nksbookstore.book.model.BookDTO;

public interface BookService {
    
    public List<BookDTO> getBooks();
    
    public BookDTO getBookById(Long id);

}
