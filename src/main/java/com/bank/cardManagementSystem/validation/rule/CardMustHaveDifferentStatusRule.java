package com.bank.cardManagementSystem.validation.rule;

import com.bank.cardManagementSystem.exception.CardException;
import com.bank.cardManagementSystem.validation.ValidationRule;
import com.bank.cardManagementSystem.validation.context.ChangeCardStatusValidationContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CardMustHaveDifferentStatusRule implements ValidationRule<ChangeCardStatusValidationContext> {

    @Override
    public void validate(ChangeCardStatusValidationContext ctx) {
        if (ctx.getCard().getStatus() == ctx.getNewStatus()) {
            log.warn("Card already has status");
            throw new CardException("Card already has status " + ctx.getCard().getStatus());
        }
    }

}
