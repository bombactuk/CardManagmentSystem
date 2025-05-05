package com.bank.cardManagementSystem.dto.response;

import com.bank.cardManagementSystem.entity.enums.CardStatus;
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

    private Long id;
    private String cardNumber;
    private CardStatus status;
    private LocalDate expirationDate;
    private BigDecimal balance;

}
