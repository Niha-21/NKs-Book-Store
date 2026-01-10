package com.nksbookstore.cart.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String errMsg) {
        super(errMsg);
    }
    
}
