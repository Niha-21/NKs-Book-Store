package com.nksbookstore.order.exception;

public class CartEmptyException extends RuntimeException {
    
    public CartEmptyException (String errMsg) {
        super(errMsg);
    }
    
}
