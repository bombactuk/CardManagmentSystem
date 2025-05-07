package com.bank.cardManagementSystem.validation.rule;

import com.bank.cardManagementSystem.exception.CardException;
import com.bank.cardManagementSystem.validation.ValidationRule;
import com.bank.cardManagementSystem.validation.context.CreateCardValidationContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CardMustNotAlreadyExistRule implements ValidationRule<CreateCardValidationContext> {

    @Override
    public void validate(CreateCardValidationContext ctx) {
        if (ctx.isCardExists()) {
            log.warn("Card already exists");
            throw new CardException("Card already exists");
        }
    }

}