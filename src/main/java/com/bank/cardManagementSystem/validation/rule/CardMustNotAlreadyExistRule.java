package com.bank.cardManagementSystem.validation.rule;

import com.bank.cardManagementSystem.exception.CardException;
import com.bank.cardManagementSystem.validation.ValidationRule;
import com.bank.cardManagementSystem.validation.context.CreateCardValidationContext;

public class CardMustNotAlreadyExistRule implements ValidationRule<CreateCardValidationContext> {

    @Override
    public void validate(CreateCardValidationContext ctx) {
        if (ctx.isCardExists()) {
            throw new CardException("Card already exists");
        }
    }

}