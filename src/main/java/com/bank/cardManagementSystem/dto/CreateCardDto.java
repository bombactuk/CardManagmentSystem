package com.bank.cardManagementSystem.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCardDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be 16 digits")
    @Pattern(regexp = "^[0-9]+$", message = "Card number must contain only digits")
    private String cardNumber;

    @NotNull(message = "Expiration date is required")
    @FutureOrPresent(message = "Expiration date must be in the future")
    private LocalDate expirationDate;

    @DecimalMin(value = "0.00", message = "Balance cannot be negative")
    private BigDecimal balance = BigDecimal.ZERO;

}
