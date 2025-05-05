package com.bank.cardManagementSystem.validation.context;

import com.bank.cardManagementSystem.dto.CreateCardDto;
import com.bank.cardManagementSystem.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCardValidationContext {

    private final CreateCardDto request;
    private final User user;
    private final boolean cardExists;

}
