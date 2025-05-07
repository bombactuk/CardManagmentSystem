package com.bank.cardManagementSystem.validation.rule;

import com.bank.cardManagementSystem.exception.CardException;
import com.bank.cardManagementSystem.validation.ValidationRule;
import com.bank.cardManagementSystem.validation.context.ChangeCardStatusValidationContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CardMustBelongToUserRule implements ValidationRule<ChangeCardStatusValidationContext> {

    @Override
    public void validate(ChangeCardStatusValidationContext ctx) {
        if (ctx.getCard().getUser() == null ||
                !ctx.getCard().getUser().getId().equals(ctx.getExpectedUserId())) {
            log.warn("Card is not linked to the specified user");
            throw new CardException("Card is not linked to the specified user");
        }
    }

}