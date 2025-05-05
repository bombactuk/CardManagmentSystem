package com.bank.cardManagementSystem.validation.context;


import com.bank.cardManagementSystem.entity.Card;
import com.bank.cardManagementSystem.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TransferValidationContext {

    private final User user;
    private final Card fromCard;
    private final Card toCard;
    private final BigDecimal amount;

}
