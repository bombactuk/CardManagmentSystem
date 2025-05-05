package com.bank.cardManagementSystem.controller;

import com.bank.cardManagementSystem.dto.request.TransferRequestDto;
import com.bank.cardManagementSystem.service.impl.CardServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("api/v1/users")
public class UserController {

    private final CardServiceImpl cardService;

    @GetMapping("/cards/{cardNumber}/balance")
    public ResponseEntity<?> getCardBalance(@PathVariable String cardNumber, Principal principal) {
        return cardService.getCardBalance(cardNumber, principal.getName());
    }

    @GetMapping("/cards")
    public ResponseEntity<?> getAllMyCards(Principal principal) {
        return cardService.getAllCardsByUsername(principal.getName());
    }

    @PutMapping("/card/block")
    public ResponseEntity<?> requestCardBlock(@RequestParam String cardNumber, Principal principal) {
        return cardService.blockCard(cardNumber, principal.getName());
    }

    @PostMapping("/cards/transfer")
    public ResponseEntity<?> transferBetweenCards(@Valid @RequestBody TransferRequestDto dto, Principal principal) {
        return cardService.transferBetweenOwnCards(principal.getName(), dto);
    }

}
