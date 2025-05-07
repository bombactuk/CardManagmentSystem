package com.bank.cardManagementSystem.dto.response;

import com.bank.cardManagementSystem.entity.enums.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardResponseDto {

    @Schema(description = "Card ID", example = "1")
    private Long id;

    @Schema(description = "Masked card number", example = "**** **** **** 1234")
    private String cardNumber;

    @Schema(description = "Card status", example = "ACTIVE")
    private CardStatus status;

    @Schema(description = "Card expiration date", example = "2026-12-31")
    private LocalDate expirationDate;

    @Schema(description = "Card balance", example = "1500.75")
    private BigDecimal balance;

}
