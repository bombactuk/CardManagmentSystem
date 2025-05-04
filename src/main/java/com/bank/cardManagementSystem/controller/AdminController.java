package com.bank.cardManagementSystem.controller;

import com.bank.cardManagementSystem.dto.CardNumberRequestDto;
import com.bank.cardManagementSystem.dto.ChangeCardStatusRequestDto;
import com.bank.cardManagementSystem.dto.CreateCardDto;
import com.bank.cardManagementSystem.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final CardService cardService;

    @PostMapping("/admin/create/cards")
    public ResponseEntity<?> createCard(@Valid @RequestBody CreateCardDto request) {
        return cardService.createCardForUser(request);
    }

    @PostMapping("/admin/card/info")
    public ResponseEntity<?> getCardUserInfo(@Valid @RequestBody CardNumberRequestDto request) {
        return cardService.getUserInfoByCardNumber(request.getCardNumber());
    }

    @PostMapping("/admin/change-status")
    public ResponseEntity<?> changeCardStatus(@Valid @RequestBody ChangeCardStatusRequestDto request) {
        return cardService.changeCardStatus(request);
    }

    @DeleteMapping("/admin/card/delete/{cardId}")
    public ResponseEntity<?> deleteUserCard(@PathVariable Long cardId) {
        return cardService.deleteCardById(cardId);
    }

}
