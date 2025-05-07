package com.bank.cardManagementSystem.controller.handler;

import com.bank.cardManagementSystem.exception.AuthException;
import com.bank.cardManagementSystem.exception.CardException;
import com.bank.cardManagementSystem.exception.ExceptionResponse;
import com.bank.cardManagementSystem.exception.enums.ErrorCode;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private ExceptionResponse exceptionBuilder(ErrorCode errorCode, String message) {
        return ExceptionResponse.builder()
                .errorcode(errorCode)
                .message(message).timestamp(LocalDateTime.now()).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation failed");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exceptionBuilder(ErrorCode.BAD_REQUEST, errorMessage));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ExceptionResponse> handleCreateUser(AuthException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exceptionBuilder(ErrorCode.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCreateUser(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exceptionBuilder(ErrorCode.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(CardException.class)
    public ResponseEntity<ExceptionResponse> handleCreateUser(CardException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exceptionBuilder(ErrorCode.BAD_REQUEST, ex.getMessage()));
    }

}