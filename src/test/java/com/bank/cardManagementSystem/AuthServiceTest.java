package com.bank.cardManagementSystem;

import com.bank.cardManagementSystem.dto.request.JwtRequestDto;
import com.bank.cardManagementSystem.dto.response.JwtResponseDto;
import com.bank.cardManagementSystem.exception.AuthException;
import com.bank.cardManagementSystem.service.impl.AuthServiceImpl;
import com.bank.cardManagementSystem.service.impl.UserServiceImpl;
import com.bank.cardManagementSystem.utils.JwtTokenUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void createAuthToken_Success() {

        JwtRequestDto request = new JwtRequestDto("user", "pass");
        UserDetails userDetails = User.builder()
                .username("user")
                .password("pass")
                .roles("USER")
                .build();

        when(userService.loadUserByUsername("user")).thenReturn(userDetails);
        when(jwtTokenUtils.generateToken(userDetails)).thenReturn("mockToken");

        ResponseEntity<?> response = authService.createAuthToken(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof JwtResponseDto);
        assertEquals("mockToken", ((JwtResponseDto) response.getBody()).getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void createAuthToken_BadCredentials() {

        JwtRequestDto request = new JwtRequestDto("user", "wrongpass");
        doThrow(BadCredentialsException.class)
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        ResponseEntity<?> response = authService.createAuthToken(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof AuthException);
        assertEquals("Incorrect login or password", ((AuthException) response.getBody()).getMessage());
    }
}
