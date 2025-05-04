package com.bank.cardManagementSystem.dto;

import com.bank.cardManagementSystem.entity.CardStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UserCardInfoDto {

    private Long cardId;
    private String maskedCardNumber;
    private String userFullName;
    private String userEmail;
    private CardStatus cardStatus;
    private BigDecimal balance;

}
