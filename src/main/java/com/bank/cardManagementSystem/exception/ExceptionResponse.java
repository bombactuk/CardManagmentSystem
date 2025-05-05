package com.bank.cardManagementSystem.exception;

import com.bank.cardManagementSystem.exception.enums.ErrorCode;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionResponse {
    private ErrorCode errorcode;
    private String message;
    private LocalDateTime timestamp;
}
