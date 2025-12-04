package com.nksbookstore.user.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String errMsg) {
        super(errMsg);
    }
    
}
