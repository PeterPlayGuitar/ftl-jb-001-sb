package com.apeter.blog.base.controller;

import com.apeter.blog.auth.exceptions.AuthException;
import com.apeter.blog.auth.exceptions.NoAccessException;
import com.apeter.blog.base.api.response.ErrorResponse;
import com.apeter.blog.user.exception.UserExistException;
import com.apeter.blog.user.exception.UserNoExistException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.sql.rowset.WebRowSet;

@ControllerAdvice
public class HandleApiExceptions extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<Object> notFoundException(ChangeSetPersister.NotFoundException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("Not found exception", HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<Object> userExistException(UserExistException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("user exists", HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(UserNoExistException.class)
    public ResponseEntity<Object> userNoExistException(UserNoExistException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("user does not exist", HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> badRequest(UserNoExistException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("ResponseStatusException", HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> authException(AuthException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("AuthException", HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(NoAccessException.class)
    public ResponseEntity<Object> authException(NoAccessException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("NoAccessException", HttpStatus.FORBIDDEN));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("Exception", HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
