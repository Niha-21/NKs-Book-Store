package com.nksbookstore.cart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse buildError(HttpStatus status, String message, WebRequest request) {
        ErrorResponse err = new ErrorResponse();
        err.setStatus(status.value());
        err.setError(status.name());
        err.setMessage(message);
        err.setPath(request.getDescription(false).replace("uri=", ""));
        return err;
    }

    // Handle illegal arguments
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgs(IllegalArgumentException ex, WebRequest req) {
        return new ResponseEntity<>(buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), req), HttpStatus.BAD_REQUEST);
    }

    // Handle Auth exceptions
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(Exception ex, WebRequest req) {
        return new ResponseEntity<>(buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), req),
                HttpStatus.UNAUTHORIZED);
    }

    // Handle Bad Credential exceptions
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(Exception ex, WebRequest req) {
        return new ResponseEntity<>(buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), req),
                HttpStatus.UNAUTHORIZED);
    }

    // Handle Cart Not Found Exception
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<?> handleCartNotFound(Exception ex, WebRequest req) {
        return new ResponseEntity<>(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), req),
                HttpStatus.NOT_FOUND);
    }

    // Handle CartItem Not Found Exception
    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<?> handleCartItemNotFound(Exception ex, WebRequest req) {
        return new ResponseEntity<>(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), req),
                HttpStatus.NOT_FOUND);
    }

    // Handle ALL exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobal(Exception ex, WebRequest req) {
        return new ResponseEntity<>(buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
