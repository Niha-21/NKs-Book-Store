package com.nksbookstore.user.exception;

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

    // Handle user already exists
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserNotFound(UserAlreadyExistsException ex, WebRequest req) {
        return new ResponseEntity<>(buildError(HttpStatus.CONFLICT, ex.getMessage(), req), HttpStatus.CONFLICT);
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

    // Handle User Not Found Exception
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(Exception ex, WebRequest req) {
        return new ResponseEntity<>(buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), req),
                HttpStatus.UNAUTHORIZED);
    }

    // Handle ALL exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobal(Exception ex, WebRequest req) {
        return new ResponseEntity<>(buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
