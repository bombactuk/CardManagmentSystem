package com.bank.cardManagementSystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CardNumberRequestDto {

    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "\\d{16}", message = "The card number must contain 16 digits.")
    @Schema(description = "Card number to search user info for", example = "1234567812345678", required = true)
    private String cardNumber;

}
