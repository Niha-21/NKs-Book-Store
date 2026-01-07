package com.nksbookstore.order.exception;

public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String errMsg) {
        super(errMsg);
    }

}
