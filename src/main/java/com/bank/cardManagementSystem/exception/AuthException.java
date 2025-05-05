package com.bank.cardManagementSystem.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }

    public AuthException(Throwable e) {
        super(e);
    }

    public AuthException(String message, Throwable e) {
        super(message, e);
    }

}
