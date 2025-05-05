package com.bank.cardManagementSystem.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CardBalanceResponseDto {
    private String cardNumber;
    private BigDecimal balance;
}