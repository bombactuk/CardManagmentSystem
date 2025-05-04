package com.bank.cardManagementSystem.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDto {

    @NotNull(message = "Source card number cannot be null")
    @Size(min = 16, max = 16, message = "Source card number must be 16 characters")
    private String fromCardNumber;

    @NotNull(message = "Destination card number cannot be null")
    @Size(min = 16, max = 16, message = "Destination card number must be 16 characters")
    private String toCardNumber;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

}
