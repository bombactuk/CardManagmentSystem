package com.bank.cardManagementSystem.service;

import com.bank.cardManagementSystem.dto.*;
import com.bank.cardManagementSystem.entity.Card;
import com.bank.cardManagementSystem.entity.CardStatus;
import com.bank.cardManagementSystem.entity.User;
import com.bank.cardManagementSystem.repository.CardRepository;
import com.bank.cardManagementSystem.repository.UserRepository;
import com.bank.cardManagementSystem.utils.CardNumberEncryptorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


import com.bank.cardManagementSystem.exception.AppError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardNumberEncryptorUtils cardNumberEncryptor;

    private ResponseEntity<AppError> errorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new AppError(status.value(), message));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private Card findCardByNumber(String cardNumber) {

        String encryptedNumber = cardNumberEncryptor.convertToDatabaseColumn(cardNumber);

        return cardRepository.findByCardNumberEncrypted(encryptedNumber)
                .orElseThrow(() -> new RuntimeException("Card not found"));
    }

    @Transactional
    public ResponseEntity<?> createCardForUser(CreateCardDto request) {

        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) {
            return errorResponse(HttpStatus.NOT_FOUND, "User not found");
        }

        if (request.getExpirationDate().isBefore(LocalDate.now())) {
            return errorResponse(HttpStatus.BAD_REQUEST, "Invalid expiration date");
        }

        String encryptedNumber = cardNumberEncryptor.convertToDatabaseColumn(request.getCardNumber());
        if (cardRepository.existsByCardNumberEncrypted(encryptedNumber)) {
            return errorResponse(HttpStatus.CONFLICT, "Card with number " + maskCardNumber(request.getCardNumber()) + " already exists");
        }

        Card card = Card.builder()
                .cardNumberEncrypted(encryptedNumber)
                .user(user)
                .expirationDate(request.getExpirationDate())
                .status(CardStatus.ACTIVE)
                .balance(request.getBalance())
                .build();

        Card savedCard = cardRepository.save(card);

        return ResponseEntity.ok(mapToResponseDTO(savedCard, request.getCardNumber()));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getUserInfoByCardNumber(String cardNumber) {

        Card card = findCardByNumber(cardNumber);
        User user = card.getUser();

        if (user == null) {
            return errorResponse(HttpStatus.NOT_FOUND, "Card owner not found");
        }

        UserCardInfoDto response = UserCardInfoDto.builder()
                .cardId(card.getId())
                .maskedCardNumber(maskCardNumber(cardNumber))
                .userFullName(user.getUsername())
                .userEmail(user.getEmail())
                .cardStatus(card.getStatus())
                .balance(card.getBalance())
                .build();

        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<?> changeCardStatus(ChangeCardStatusRequestDto request) {

        Card card = cardRepository.findById(request.getCardId()).orElse(null);

        if (card == null) {
            return errorResponse(HttpStatus.NOT_FOUND, "Card not found");
        }

        if (card.getUser() == null || !card.getUser().getId().equals(request.getUserId())) {
            return errorResponse(HttpStatus.BAD_REQUEST, "Card is not linked to the specified user");
        }

        if (card.getStatus() == request.getNewStatus()) {
            return errorResponse(HttpStatus.BAD_REQUEST, "Card already has status " + card.getStatus());
        }

        card.setStatus(request.getNewStatus());
        cardRepository.save(card);

        return ResponseEntity.ok("Card status successfully changed to " + card.getStatus());
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getCardBalance(String cardNumber, String username) {

        Card card = findCardByNumber(cardNumber);

        User user = findUserByUsername(username);
        if (!card.getUser().getId().equals(user.getId())) {
            return errorResponse(HttpStatus.FORBIDDEN, "The card does not belong to the current user");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("cardNumber", maskCardNumber(cardNumber));
        response.put("balance", card.getBalance());

        return ResponseEntity.ok(response);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getAllCardsByUsername(String username) {

        List<Card> cards = cardRepository.findAllByUserUsername(username);

        List<CardResponseDto> cardList = cards.stream()
                .map(card -> CardResponseDto.builder()
                        .id(card.getId())
                        .cardNumber(maskCardNumber(cardNumberEncryptor.convertToEntityAttribute(card.getCardNumberEncrypted())))
                        .expirationDate(card.getExpirationDate())
                        .status(card.getStatus())
                        .balance(card.getBalance())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(cardList);
    }

    @Transactional
    public ResponseEntity<?> deleteCardById(Long cardId) {

        Optional<Card> cardOptional = cardRepository.findById(cardId);
        if (cardOptional.isEmpty()) {
            return errorResponse(HttpStatus.NOT_FOUND, "Card not found");
        }

        cardRepository.deleteById(cardId);

        return ResponseEntity.ok("Card deleted successfully");
    }

    @Transactional
    public ResponseEntity<?> blockCard(String rawCardNumber, String username) {

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return errorResponse(HttpStatus.UNAUTHORIZED, "User not found");
        }

        String encryptedNumber = cardNumberEncryptor.convertToDatabaseColumn(rawCardNumber);
        Optional<Card> cardOptional = cardRepository.findByCardNumberEncrypted(encryptedNumber);

        if (cardOptional.isEmpty()) {
            return errorResponse(HttpStatus.NOT_FOUND, "Card not found");
        }

        Card card = cardOptional.get();

        if (!card.getUser().getId().equals(userOptional.get().getId())) {
            return errorResponse(HttpStatus.FORBIDDEN, "You are not the owner of the card");
        }

        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);

        return ResponseEntity.ok("The card was successfully blocked");
    }

    @Transactional
    public ResponseEntity<?> transferBetweenOwnCards(String username, TransferRequestDto dto) {

        User user = findUserByUsername(username);

        Card fromCard = findCardByNumber(dto.getFromCardNumber());
        Card toCard = findCardByNumber(dto.getToCardNumber());

        if (!fromCard.getUser().equals(user) || !toCard.getUser().equals(user)) {
            return errorResponse(HttpStatus.FORBIDDEN, "One or both cards do not belong to user");
        }

        if (fromCard.getStatus() == CardStatus.BLOCKED || fromCard.getStatus() == CardStatus.EXPIRED) {
            return errorResponse(HttpStatus.BAD_REQUEST, "Source card is either blocked or expired, transfer is not allowed");
        }

        if (toCard.getStatus() == CardStatus.BLOCKED || toCard.getStatus() == CardStatus.EXPIRED) {
            return errorResponse(HttpStatus.BAD_REQUEST, "Target card is either blocked or expired, transfer is not allowed");
        }

        if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return errorResponse(HttpStatus.BAD_REQUEST, "Amount must be greater than zero");
        }

        if (fromCard.getBalance().compareTo(dto.getAmount()) < 0) {
            return errorResponse(HttpStatus.BAD_REQUEST, "Insufficient funds on source card");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(dto.getAmount()));
        toCard.setBalance(toCard.getBalance().add(dto.getAmount()));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);

        return ResponseEntity.ok("Transfer completed successfully");
    }

    private String maskCardNumber(String cardNumber) {

        if (cardNumber == null || cardNumber.length() < 4) {
            return "**** **** **** ****";
        }

        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

    public CardResponseDto mapToResponseDTO(Card card, String originalCardNumber) {
        return CardResponseDto.builder()
                .id(card.getId())
                .cardNumber(maskCardNumber(originalCardNumber))
                .expirationDate(card.getExpirationDate())
                .status(card.getStatus())
                .balance(card.getBalance())
                .build();
    }

}