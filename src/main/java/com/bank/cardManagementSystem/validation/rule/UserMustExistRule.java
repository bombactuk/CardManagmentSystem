package com.bank.cardManagementSystem.validation.rule;

import com.bank.cardManagementSystem.exception.CardException;
import com.bank.cardManagementSystem.validation.ValidationRule;
import com.bank.cardManagementSystem.validation.context.CreateCardValidationContext;

public class UserMustExistRule implements ValidationRule<CreateCardValidationContext> {

    @Override
    public void validate(CreateCardValidationContext ctx) {
        if (ctx.getUser() == null) {
            throw new CardException("User not found");
        }
    }

}