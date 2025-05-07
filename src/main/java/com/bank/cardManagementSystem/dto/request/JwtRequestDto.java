package com.bank.cardManagementSystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO for user authentication")
public class JwtRequestDto {

    @Schema(description = "Username of the user for authentication", example = "user123", required = true)
    private String username;

    @Schema(description = "Password of the user for authentication", example = "password123", required = true)
    private String password;

}
