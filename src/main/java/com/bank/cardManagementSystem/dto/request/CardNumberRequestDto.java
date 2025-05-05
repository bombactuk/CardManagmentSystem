package com.bank.cardManagementSystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CardNumberRequestDto {

    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "\\d{16}", message = "The card number must contain 16 digits.")
    private String cardNumber;

}
