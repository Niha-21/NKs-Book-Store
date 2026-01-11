package com.nksbookstore.cart.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.nksbookstore.cart.client.BookClient;
import com.nksbookstore.cart.exception.BookNotFoundException;
import com.nksbookstore.cart.exception.BookServiceUnavailableException;
import com.nksbookstore.cart.exception.UnauthorizedException;
import com.nksbookstore.cart.model.BookDTO;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookClientService {
    
    private final BookClient bookClient;

    private static final String BOOK_CB = "bookService";

    @CircuitBreaker(name = BOOK_CB, fallbackMethod = "getBookByIdFallBack")
    public BookDTO getBookById(@PathVariable Long bookId) {
        return bookClient.getBookById(bookId);
    }

    public BookDTO getBookByIdFallBack(Long bookId, Throwable ex) {

        if(ex instanceof FeignException.NotFound e) {
            log.error("Book not found. bookId={}, status={}, message={}",
            bookId, e.status(), e.getMessage());
            throw new BookNotFoundException("Book Not Found");
        } else if(ex instanceof FeignException e) {
            log.error("Book service call failed. bookId={}, status={}, message={}",
            bookId, e.status(), e.getMessage());
            if (e.status() == 401 || e.status() == 403) {
                throw new UnauthorizedException("Unauthorized to access book-service");           
            }
            throw new BookServiceUnavailableException("Book service unavailable");
        } else {
            log.error("Book Service Unavailable | Failed to get bookId={}" + bookId, ex);
            throw new BookServiceUnavailableException("Book service unavailable");
        }
    }
    
}
