package com.nksbookstore.cart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nksbookstore.cart.model.BookDTO;

@FeignClient(name = "book-service", url = "${book.service.url}")
public interface BookClient {

    @GetMapping("/books/{bookId}")
    BookDTO getBookById(@PathVariable Long bookId);
}
