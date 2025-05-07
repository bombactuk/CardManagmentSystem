package com.bank.cardManagementSystem.validation.rule;

import com.bank.cardManagementSystem.exception.CardException;
import com.bank.cardManagementSystem.validation.ValidationRule;
import com.bank.cardManagementSystem.validation.context.TransferValidationContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CardOwnershipRule implements ValidationRule<TransferValidationContext> {

    @Override
    public void validate(TransferValidationContext ctx) {
        if (!ctx.getFromCard().getUser().equals(ctx.getUser()) ||
                !ctx.getToCard().getUser().equals(ctx.getUser())) {
            log.warn("One or both cards do not belong to user");
            throw new CardException("One or both cards do not belong to user");
        }
    }

}
