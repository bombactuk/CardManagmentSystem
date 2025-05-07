package com.bank.cardManagementSystem.validation.rule;

import com.bank.cardManagementSystem.exception.CardException;
import com.bank.cardManagementSystem.validation.ValidationRule;
import com.bank.cardManagementSystem.validation.context.TransferValidationContext;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class AmountPositiveRule implements ValidationRule<TransferValidationContext> {

    @Override
    public void validate(TransferValidationContext ctx) {
        if (ctx.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Amount must be greater than zero");
            throw new CardException("Amount must be greater than zero");
        }
    }

}
