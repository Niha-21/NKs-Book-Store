package com.nksbookstore.cart.exception;

public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String errMsg) {
        super(errMsg);
    }

}
