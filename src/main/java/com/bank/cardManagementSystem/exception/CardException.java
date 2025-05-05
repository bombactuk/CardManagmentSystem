package com.bank.cardManagementSystem.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CardException extends RuntimeException {

    public CardException(String message) {
        super(message);
    }

    public CardException(Throwable e) {
        super(e);
    }

    public CardException(String message, Throwable e) {
        super(message, e);
    }

}
