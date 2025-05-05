package com.bank.cardManagementSystem.validation;

import java.util.ArrayList;
import java.util.List;

public class Validator<T> {

    private final List<ValidationRule<T>> rules = new ArrayList<>();

    public Validator<T> addRule(ValidationRule<T> rule) {
        rules.add(rule);
        return this;
    }

    public void validate(T context) {
        for (ValidationRule<T> rule : rules) {
            rule.validate(context);
        }
    }

}
