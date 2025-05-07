package com.bank.cardManagementSystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "DTO for transferring money between cards")
    private String fromCardNumber;

    @NotNull(message = "Destination card number cannot be null")
    @Size(min = 16, max = 16, message = "Destination card number must be 16 characters")
    @Schema(description = "Card number to transfer funds to", example = "8765432187654321", required = true)
    private String toCardNumber;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Schema(description = "Amount of money to transfer", example = "100.00", required = true, minimum = "0.01")
    private BigDecimal amount;

}
