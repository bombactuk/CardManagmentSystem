package com.bank.cardManagementSystem.dto;

import com.bank.cardManagementSystem.entity.enums.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UserCardInfoDto {

    @Schema(description = "Card ID", example = "101")
    private Long cardId;

    @Schema(description = "Masked card number", example = "1234********5678")
    private String maskedCardNumber;

    @Schema(description = "Full name or username of the card owner", example = "john.doe")
    private String userFullName;

    @Schema(description = "Email of the card owner", example = "john.doe@example.com")
    private String userEmail;

    @Schema(description = "Card status", example = "ACTIVE")
    private CardStatus cardStatus;

    @Schema(description = "Current balance of the card", example = "2500.00")
    private BigDecimal balance;

}
