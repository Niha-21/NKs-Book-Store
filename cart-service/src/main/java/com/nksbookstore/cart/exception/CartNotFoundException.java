package com.nksbookstore.cart.exception;

public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(String errMsg) {
        super(errMsg);
    }
    
}
