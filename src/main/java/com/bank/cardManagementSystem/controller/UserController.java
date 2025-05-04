package com.bank.cardManagementSystem.controller;

import com.bank.cardManagementSystem.dto.TransferRequestDto;
import com.bank.cardManagementSystem.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final CardService cardService;

    @GetMapping("/user/cards/{cardNumber}/balance")
    public ResponseEntity<?> getCardBalance(@PathVariable String cardNumber, Principal principal) {
        return cardService.getCardBalance(cardNumber, principal.getName());
    }

    @GetMapping("/user/cards")
    public ResponseEntity<?> getAllMyCards(Principal principal) {
        return cardService.getAllCardsByUsername(principal.getName());
    }

    @PutMapping("/user/card/block")
    public ResponseEntity<?> requestCardBlock(@RequestParam String cardNumber, Principal principal) {
        return cardService.blockCard(cardNumber, principal.getName());
    }

    @PostMapping("/user/cards/transfer")
    public ResponseEntity<?> transferBetweenCards(@Valid @RequestBody TransferRequestDto dto, Principal principal) {
        return cardService.transferBetweenOwnCards(principal.getName(), dto);
    }

}
