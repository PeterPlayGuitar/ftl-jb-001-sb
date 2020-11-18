package com.apeter.blog.base.controller;

import com.apeter.blog.base.api.response.ErrorResponse;
import com.apeter.blog.user.exception.UserExistException;
import com.apeter.blog.user.exception.UserNoExistException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> userNoExistException(Exception ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("Exception", HttpStatus.INTERNAL_SERVER_ERROR));
    }

}