package com.nksbookstore.book.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String errMsg) {
        super(errMsg);
    }
    
}
