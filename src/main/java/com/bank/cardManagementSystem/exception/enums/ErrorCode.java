package com.bank.cardManagementSystem.exception.enums;


import lombok.Getter;

@Getter
public enum ErrorCode {

    BAD_REQUEST("Error valid"),
    UNAUTHORIZED("Error Unauthorized"),
    NOT_FOUND("Error being in the passage");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

}
