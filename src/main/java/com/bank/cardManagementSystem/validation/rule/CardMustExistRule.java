package com.bank.cardManagementSystem.validation.rule;

import com.bank.cardManagementSystem.exception.CardException;
import com.bank.cardManagementSystem.validation.ValidationRule;
import com.bank.cardManagementSystem.validation.context.ChangeCardStatusValidationContext;

public class CardMustExistRule implements ValidationRule<ChangeCardStatusValidationContext> {

    @Override
    public void validate(ChangeCardStatusValidationContext ctx) {
        if (ctx.getCard() == null) {
            throw new CardException("Card not found");
        }
    }

}
