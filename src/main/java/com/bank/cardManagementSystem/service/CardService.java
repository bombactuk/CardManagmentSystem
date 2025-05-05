package com.bank.cardManagementSystem.service;

import com.bank.cardManagementSystem.dto.request.ChangeCardStatusRequestDto;
import com.bank.cardManagementSystem.dto.request.TransferRequestDto;
import com.bank.cardManagementSystem.dto.CreateCardDto;
import org.springframework.http.ResponseEntity;

public interface CardService {

    ResponseEntity<?> createCardForUser(CreateCardDto request);

    ResponseEntity<?> getUserInfoByCardNumber(String cardNumber);

    ResponseEntity<?> changeCardStatus(ChangeCardStatusRequestDto request);

    ResponseEntity<?> getCardBalance(String cardNumber, String username);

    ResponseEntity<?> getAllCardsByUsername(String username);

    ResponseEntity<?> deleteCardById(Long cardId);

    ResponseEntity<?> blockCard(String rawCardNumber, String username);

    ResponseEntity<?> transferBetweenOwnCards(String username, TransferRequestDto dto);

}
