package ru.AMosk.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.AMosk.exception.AuthException;
import ru.AMosk.exception.ErrorResponse;
import ru.AMosk.exception.UploadException;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(AuthException.class)
    private ResponseEntity<ErrorResponse> errorAuth(AuthException e) {
        ErrorResponse errorResponse = ErrorResponse.getInstance();
        errorResponse.setMessage(e.getMessage(),e.getId());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UploadException.class)
    private ResponseEntity<ErrorResponse> errorAuth(UploadException e) {
        ErrorResponse errorResponse = ErrorResponse.getInstance();
        errorResponse.setMessage(e.getMessage(),e.getId());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}


