package com.bank.cardManagementSystem.service.impl;

import com.bank.cardManagementSystem.dto.*;
import com.bank.cardManagementSystem.dto.request.ChangeCardStatusRequestDto;
import com.bank.cardManagementSystem.dto.request.TransferRequestDto;
import com.bank.cardManagementSystem.dto.response.CardResponseDto;
import com.bank.cardManagementSystem.entity.Card;
import com.bank.cardManagementSystem.entity.enums.CardStatus;
import com.bank.cardManagementSystem.entity.User;
import com.bank.cardManagementSystem.exception.CardException;
import com.bank.cardManagementSystem.repository.CardRepository;
import com.bank.cardManagementSystem.repository.UserRepository;
import com.bank.cardManagementSystem.service.CardService;
import com.bank.cardManagementSystem.utils.CardNumberEncryptorUtils;
import com.bank.cardManagementSystem.validation.Validator;
import com.bank.cardManagementSystem.validation.context.ChangeCardStatusValidationContext;
import com.bank.cardManagementSystem.validation.context.CreateCardValidationContext;
import com.bank.cardManagementSystem.validation.context.TransferValidationContext;
import com.bank.cardManagementSystem.validation.rule.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardNumberEncryptorUtils cardNumberEncryptor;

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private Card findCardByNumber(String cardNumber) {

        String encryptedNumber = cardNumberEncryptor.convertToDatabaseColumn(cardNumber);

        return cardRepository.findByCardNumberEncrypted(encryptedNumber)
                .orElseThrow(() -> new CardException("Card not found"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> createCardForUser(CreateCardDto request) {

        User user = userRepository.findById(request.getUserId()).orElse(null);
        String encryptedNumber = cardNumberEncryptor.convertToDatabaseColumn(request.getCardNumber());
        boolean cardExists = cardRepository.existsByCardNumberEncrypted(encryptedNumber);

        CreateCardValidationContext context = new CreateCardValidationContext(request, user, cardExists);

        Validator<CreateCardValidationContext> validator = new Validator<>();
        validator
                .addRule(new UserMustExistRule())
                .addRule(new ExpirationDateMustBeValidRule())
                .addRule(new CardMustNotAlreadyExistRule());

        validator.validate(context);

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

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> getUserInfoByCardNumber(String cardNumber) {

        Card card = findCardByNumber(cardNumber);
        User user = card.getUser();

        if (user == null) {
            throw new CardException("Card owner not found");
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

    @Override
    @Transactional
    public ResponseEntity<?> changeCardStatus(ChangeCardStatusRequestDto request) {

        Card card = cardRepository.findById(request.getCardId()).orElse(null);

        ChangeCardStatusValidationContext context = new ChangeCardStatusValidationContext(
                card, request.getUserId(), request.getNewStatus());

        new Validator<ChangeCardStatusValidationContext>()
                .addRule(new CardMustExistRule())
                .addRule(new CardMustBelongToUserRule())
                .addRule(new CardMustHaveDifferentStatusRule())
                .validate(context);

        card.setStatus(request.getNewStatus());
        cardRepository.save(card);

        return ResponseEntity.ok("Card status successfully changed to " + card.getStatus());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> getCardBalance(String cardNumber, String username) {

        Card card = findCardByNumber(cardNumber);

        User user = findUserByUsername(username);
        if (!card.getUser().getId().equals(user.getId())) {
            throw new CardException("The card does not belong to the current user");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("cardNumber", maskCardNumber(cardNumber));
        response.put("balance", card.getBalance());

        return ResponseEntity.ok(response);
    }

    @Override
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

    @Override
    @Transactional
    public ResponseEntity<?> deleteCardById(Long cardId) {

        Optional<Card> cardOptional = cardRepository.findById(cardId);
        if (cardOptional.isEmpty()) {
            throw new CardException("Card not found");
        }

        cardRepository.deleteById(cardId);

        return ResponseEntity.ok("Card deleted successfully");
    }

    @Override
    @Transactional
    public ResponseEntity<?> blockCard(String rawCardNumber, String username) {

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        String encryptedNumber = cardNumberEncryptor.convertToDatabaseColumn(rawCardNumber);
        Optional<Card> cardOptional = cardRepository.findByCardNumberEncrypted(encryptedNumber);

        Card card = cardOptional.get();

        if (!card.getUser().getId().equals(userOptional.get().getId())) {
            throw new CardException("You are not the owner of the card");
        }

        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);

        return ResponseEntity.ok("The card was successfully blocked");
    }

    @Override
    @Transactional
    public ResponseEntity<?> transferBetweenOwnCards(String username, TransferRequestDto dto) {

        User user = findUserByUsername(username);

        Card fromCard = findCardByNumber(dto.getFromCardNumber());
        Card toCard = findCardByNumber(dto.getToCardNumber());

        TransferValidationContext context = new TransferValidationContext(user, fromCard, toCard, dto.getAmount());

        new Validator<TransferValidationContext>()
                .addRule(new CardOwnershipRule())
                .addRule(new CardStatusRule())
                .addRule(new AmountPositiveRule())
                .addRule(new SufficientFundsRule())
                .validate(context);

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