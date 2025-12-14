package com.nksbookstore.cart.exception;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorResponse {
    private LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String error;
    private String message;
    private String path;
}
