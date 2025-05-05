package com.bank.cardManagementSystem.validation.context;

import com.bank.cardManagementSystem.entity.Card;
import com.bank.cardManagementSystem.entity.enums.CardStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeCardStatusValidationContext {

    private final Card card;
    private final Long expectedUserId;
    private final CardStatus newStatus;

}
