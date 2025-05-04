package com.bank.cardManagementSystem.dto;

import com.bank.cardManagementSystem.entity.CardStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeCardStatusRequestDto {

    @NotNull(message = "ID card required")
    private Long cardId;

    @NotNull(message = "User ID required")
    private Long userId;

    @NotNull(message = "Status required")
    private CardStatus newStatus;

}
