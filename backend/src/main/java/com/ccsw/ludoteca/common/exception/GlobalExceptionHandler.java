package com.ccsw.ludoteca.common.exception;

import com.ccsw.ludoteca.author.AuthorException;
import com.ccsw.ludoteca.dto.StatusResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorException.class)
    public ResponseEntity<StatusResponse> handleAuthorException(AuthorException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex.getMessage().equals(AuthorException.AUTHOR_ID_NOT_FOUND) || ex.getMessage().equals(AuthorException.AUTHOR_HAS_GAMES) || ex.getMessage().equals(CommonErrorResponse.MISSING_REQUIRED_FIELDS)) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex.getMessage().equals(CommonErrorResponse.DEFAULT_ERROR)) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(status).body(new StatusResponse(ex.getMessage(), ex.getExtendedMessage()));
    }
}