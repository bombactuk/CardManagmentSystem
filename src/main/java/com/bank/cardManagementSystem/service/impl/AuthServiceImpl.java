package com.bank.cardManagementSystem.service.impl;

import com.bank.cardManagementSystem.dto.request.JwtRequestDto;
import com.bank.cardManagementSystem.dto.response.JwtResponseDto;
import com.bank.cardManagementSystem.dto.RegistrationUserDto;
import com.bank.cardManagementSystem.dto.UserDto;
import com.bank.cardManagementSystem.entity.User;
import com.bank.cardManagementSystem.exception.AuthException;
import com.bank.cardManagementSystem.service.AuthService;
import com.bank.cardManagementSystem.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<?> createAuthToken(JwtRequestDto authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new AuthException("Incorrect login or password");
        }

        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponseDto(token));
    }

    @Override
    public ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            throw new AuthException("Passwords do not match");
        }

        if (userService.findByUsername(registrationUserDto.getUsername()).isPresent()) {
            throw new AuthException("The user already exists");
        }

        User user = userService.createNewUser(registrationUserDto);

        return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername(), user.getEmail()));
    }
}