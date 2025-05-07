package com.bank.cardManagementSystem.controller;

import com.bank.cardManagementSystem.dto.request.JwtRequestDto;
import com.bank.cardManagementSystem.dto.RegistrationUserDto;
import com.bank.cardManagementSystem.service.impl.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/clients")
@Tag(name = "Authentication", description = "Operations related to user authentication and Registering.")
public class AuthController {

    private final AuthServiceImpl authService;

    @Operation(
            summary = "Authenticate user and generate JWT token",
            description = "This endpoint allows users to authenticate using their username and password. If successful, it returns a JWT token that can be used for subsequent API requests.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authentication successful, JWT token generated"),
                    @ApiResponse(responseCode = "400", description = "Bad credentials, authentication failed"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequestDto authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @Operation(
            summary = "Register a new user",
            description = "This endpoint allows the registration of a new user by providing a username, password, confirmPassword, and email."
    )
    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.createNewUser(registrationUserDto);
    }

}
