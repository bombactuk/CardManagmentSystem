package com.bank.cardManagementSystem.validation.rule;

import com.bank.cardManagementSystem.exception.CardException;
import com.bank.cardManagementSystem.validation.ValidationRule;
import com.bank.cardManagementSystem.validation.context.TransferValidationContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SufficientFundsRule implements ValidationRule<TransferValidationContext> {

    @Override
    public void validate(TransferValidationContext ctx) {
        if (ctx.getFromCard().getBalance().compareTo(ctx.getAmount()) < 0) {
            log.warn("Insufficient funds on source card");
            throw new CardException("Insufficient funds on source card");
        }
    }

}
