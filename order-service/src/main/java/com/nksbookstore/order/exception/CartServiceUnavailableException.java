package com.nksbookstore.order.exception;

public class CartServiceUnavailableException extends RuntimeException {
    
    public CartServiceUnavailableException(String errMsg) {
        super(errMsg);
    }

}
