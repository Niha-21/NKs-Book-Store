package com.nksbookstore.book.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nksbookstore.book.entity.Book;
import com.nksbookstore.book.exception.BookNotFoundException;
import com.nksbookstore.book.model.BookDTO;
import com.nksbookstore.book.repository.BooksRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BooksRepository booksRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void getBooks_shouldReturnMappedBookDTOList() {
        Book book1 = new Book(1L, "Book 1", "Desc 1", "img1", BigDecimal.valueOf(100));
        Book book2 = new Book(2L, "Book 2", "Desc 2", "img2", BigDecimal.valueOf(200));

        when(booksRepository.findAll()).thenReturn(List.of(book1, book2));

        List<BookDTO> result = bookService.getBooks();

        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals(BigDecimal.valueOf(200), result.get(1).getPrice());
    }

    @Test
    void getBooks_shouldReturnEmptyListWhenNoBooksExist() {
        when(booksRepository.findAll()).thenReturn(List.of());

        List<BookDTO> result = bookService.getBooks();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getBookById_shouldReturnBookDTO() {
        Book book = new Book(1L, "Clean Code", "Programming", "img", BigDecimal.valueOf(450));

        when(booksRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDTO result = bookService.getBookById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Clean Code", result.getTitle());
        assertEquals(BigDecimal.valueOf(450), result.getPrice());
    }

    @Test
    void getBookById_shouldThrowExceptionWhenBookNotFound() {
        when(booksRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> bookService.getBookById(99L));
    }
}
