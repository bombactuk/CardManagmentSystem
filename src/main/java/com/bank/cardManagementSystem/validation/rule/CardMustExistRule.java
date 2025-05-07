package com.bank.cardManagementSystem.validation.rule;

import com.bank.cardManagementSystem.exception.CardException;
import com.bank.cardManagementSystem.validation.ValidationRule;
import com.bank.cardManagementSystem.validation.context.ChangeCardStatusValidationContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CardMustExistRule implements ValidationRule<ChangeCardStatusValidationContext> {

    @Override
    public void validate(ChangeCardStatusValidationContext ctx) {
        if (ctx.getCard() == null) {
            log.warn("Card not found");
            throw new CardException("Card not found");
        }
    }

}
