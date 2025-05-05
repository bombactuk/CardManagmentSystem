package com.bank.cardManagementSystem.controller;

import com.bank.cardManagementSystem.dto.request.CardNumberRequestDto;
import com.bank.cardManagementSystem.dto.request.ChangeCardStatusRequestDto;
import com.bank.cardManagementSystem.dto.CreateCardDto;
import com.bank.cardManagementSystem.service.impl.CardServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("api/v1/admins")
public class AdminController {

    private final CardServiceImpl cardService;

    @PostMapping("/create/cards")
    public ResponseEntity<?> createCard(@Valid @RequestBody CreateCardDto request) {
        return cardService.createCardForUser(request);
    }

    @PostMapping("/card/info")
    public ResponseEntity<?> getCardUserInfo(@Valid @RequestBody CardNumberRequestDto request) {
        return cardService.getUserInfoByCardNumber(request.getCardNumber());
    }

    @PostMapping("/change-status")
    public ResponseEntity<?> changeCardStatus(@Valid @RequestBody ChangeCardStatusRequestDto request) {
        return cardService.changeCardStatus(request);
    }

    @DeleteMapping("/card/delete/{cardId}")
    public ResponseEntity<?> deleteUserCard(@PathVariable Long cardId) {
        return cardService.deleteCardById(cardId);
    }

}
