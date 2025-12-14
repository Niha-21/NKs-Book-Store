package com.nksbookstore.cart.exception;

public class CartItemNotFoundException extends RuntimeException {

    public CartItemNotFoundException(String errMsg) {
        super(errMsg);
    }
    
}
