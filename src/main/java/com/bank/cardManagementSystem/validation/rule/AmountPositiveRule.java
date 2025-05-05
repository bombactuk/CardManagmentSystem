package com.bank.cardManagementSystem.validation.rule;

import com.bank.cardManagementSystem.exception.CardException;
import com.bank.cardManagementSystem.validation.ValidationRule;
import com.bank.cardManagementSystem.validation.context.TransferValidationContext;

import java.math.BigDecimal;

public class AmountPositiveRule implements ValidationRule<TransferValidationContext> {

    @Override
    public void validate(TransferValidationContext ctx) {
        if (ctx.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CardException("Amount must be greater than zero");
        }
    }

}
