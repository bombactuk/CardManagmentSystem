package com.bank.cardManagementSystem.validation.rule;

import com.bank.cardManagementSystem.entity.enums.CardStatus;
import com.bank.cardManagementSystem.exception.CardException;
import com.bank.cardManagementSystem.validation.ValidationRule;
import com.bank.cardManagementSystem.validation.context.TransferValidationContext;

public class CardStatusRule implements ValidationRule<TransferValidationContext> {

    @Override
    public void validate(TransferValidationContext ctx) {
        if (ctx.getFromCard().getStatus() == CardStatus.BLOCKED ||
                ctx.getFromCard().getStatus() == CardStatus.EXPIRED) {
            throw new CardException("Source card is either blocked or expired");
        }

        if (ctx.getToCard().getStatus() == CardStatus.BLOCKED ||
                ctx.getToCard().getStatus() == CardStatus.EXPIRED) {
            throw new CardException("Target card is either blocked or expired");
        }
    }

}
