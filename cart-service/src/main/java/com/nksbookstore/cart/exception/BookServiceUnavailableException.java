package com.nksbookstore.cart.exception;

public class BookServiceUnavailableException extends RuntimeException {
    
    public BookServiceUnavailableException(String errMsg) {
        super(errMsg);
    }

}
