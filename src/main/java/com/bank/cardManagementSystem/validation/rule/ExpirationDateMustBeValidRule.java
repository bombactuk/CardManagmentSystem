package com.bank.cardManagementSystem.validation.rule;

import com.bank.cardManagementSystem.exception.CardException;
import com.bank.cardManagementSystem.validation.ValidationRule;
import com.bank.cardManagementSystem.validation.context.CreateCardValidationContext;

import java.time.LocalDate;

public class ExpirationDateMustBeValidRule implements ValidationRule<CreateCardValidationContext> {

    @Override
    public void validate(CreateCardValidationContext ctx) {
        if (ctx.getRequest().getExpirationDate().isBefore(LocalDate.now())) {
            throw new CardException("Invalid expiration date");
        }
    }

}
