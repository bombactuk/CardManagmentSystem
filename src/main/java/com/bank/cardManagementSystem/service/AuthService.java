package com.bank.cardManagementSystem.service;

import com.bank.cardManagementSystem.dto.RegistrationUserDto;
import com.bank.cardManagementSystem.dto.request.JwtRequestDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> createAuthToken(JwtRequestDto authRequest);

    ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto);

}
