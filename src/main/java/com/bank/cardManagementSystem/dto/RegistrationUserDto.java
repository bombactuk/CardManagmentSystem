package com.bank.cardManagementSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO for registering a new user")
public class RegistrationUserDto {

    @Schema(description = "The username of the new user", example = "user123", required = true)
    private String username;

    @Schema(description = "The password of the new user", example = "password123", required = true)
    private String password;

    @Schema(description = "The confirmation of the user's password", example = "password123", required = true)
    private String confirmPassword;

    @Schema(description = "The email address of the new user", example = "user123@example.com", required = true)
    private String email;

}
