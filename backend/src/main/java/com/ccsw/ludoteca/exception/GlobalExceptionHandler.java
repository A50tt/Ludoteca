package com.ccsw.ludoteca.exception;

import com.ccsw.ludoteca.author.AuthorException;
import com.ccsw.ludoteca.category.CategoryException;
import com.ccsw.ludoteca.client.ClientException;
import com.ccsw.ludoteca.dto.StatusResponse;
import com.ccsw.ludoteca.game.model.GameException;
import com.ccsw.ludoteca.loan.LoanException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    @ExceptionHandler(AuthorException.class)
    public ResponseEntity<StatusResponse> handleAuthorException(AuthorException ex) {

        if (ex.getMessage().equals(AuthorException.AUTHOR_ID_NOT_FOUND) || ex.getMessage().equals(AuthorException.AUTHOR_HAS_GAMES) || ex.getMessage().equals(CommonErrorResponse.MISSING_REQUIRED_FIELDS)) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(status).body(new StatusResponse(ex.getMessage(), ex.getExtendedMessage()));
    }

    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<StatusResponse> handleCategoryException(CategoryException ex) {
        if (ex.getMessage().equals(CategoryException.CATEGORY_HAS_GAMES) || ex.getMessage().equals(CategoryException.CATEGORY_ID_NOT_FOUND) || ex.getMessage().equals(CommonErrorResponse.MISSING_REQUIRED_FIELDS)) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(status).body(new StatusResponse(ex.getMessage(), ex.getExtendedMessage()));
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<StatusResponse> handleClientException(ClientException ex) {
        if (ex.getMessage().equals(ClientException.CLIENT_ID_NOT_FOUND) || ex.getMessage().equals(ClientException.CLIENT_HAS_GAMES) || ex.getMessage().equals(ClientException.NAME_ALREADY_EXISTS)) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(status).body(new StatusResponse(ex.getMessage(), ex.getExtendedMessage()));
    }

    @ExceptionHandler(GameException.class)
    public ResponseEntity<StatusResponse> handleGameException(GameException ex) {
        if (ex.getMessage().equals(CommonErrorResponse.DEFAULT_ERROR)) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(status).body(new StatusResponse(ex.getMessage(), ex.getExtendedMessage()));
    }

    @ExceptionHandler(LoanException.class)
    public ResponseEntity<StatusResponse> handleLoanException(LoanException ex) {
        if (ex.getMessage().equals(LoanException.ID_NOT_EXIST) || ex.getMessage().equals(LoanException.INVALID_END_DATE) || ex.getMessage().equals(LoanException.INVALID_PERIOD) || ex.getMessage().equals(LoanException.GAME_ALREADY_LENT) || ex.getMessage().equals(LoanException.LOAN_LIMIT_EXCEEDED)) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(status).body(new StatusResponse(ex.getMessage(), ex.getExtendedMessage()));
    }
}