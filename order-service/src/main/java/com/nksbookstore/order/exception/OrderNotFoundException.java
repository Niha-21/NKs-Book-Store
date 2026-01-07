package com.nksbookstore.order.exception;

public class OrderNotFoundException extends RuntimeException {
    
    public OrderNotFoundException(String errMsg) {
        super(errMsg);
    }

}
