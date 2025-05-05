package com.bank.cardManagementSystem.validation;

public interface ValidationRule<T> {
    void validate(T context);
}
