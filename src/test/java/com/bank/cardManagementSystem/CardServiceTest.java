package com.bank.cardManagementSystem;

import com.bank.cardManagementSystem.dto.*;
import com.bank.cardManagementSystem.entity.Card;
import com.bank.cardManagementSystem.entity.CardStatus;
import com.bank.cardManagementSystem.entity.User;
import com.bank.cardManagementSystem.repository.CardRepository;
import com.bank.cardManagementSystem.repository.UserRepository;
import com.bank.cardManagementSystem.service.CardService;
import com.bank.cardManagementSystem.utils.CardNumberEncryptorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceTest {

    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardNumberEncryptorUtils encryptor;

    private final String rawCardNumber = "1234567890123456";
    private final String encryptedCardNumber = "ENCRYPTED";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCardForUser_shouldReturnOk_whenUserExistsAndCardIsValid() {
        CreateCardDto dto = new CreateCardDto();
        dto.setUserId(1L);
        dto.setCardNumber(rawCardNumber);
        dto.setExpirationDate(LocalDate.now().plusYears(1));
        dto.setBalance(new BigDecimal("100.00"));

        User user = new User();
        user.setId(1L);

        Card card = Card.builder()
                .id(10L)
                .cardNumberEncrypted(encryptedCardNumber)
                .user(user)
                .expirationDate(dto.getExpirationDate())
                .status(CardStatus.ACTIVE)
                .balance(dto.getBalance())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(encryptor.convertToDatabaseColumn(rawCardNumber)).thenReturn(encryptedCardNumber);
        when(cardRepository.existsByCardNumberEncrypted(encryptedCardNumber)).thenReturn(false);
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        ResponseEntity<?> response = cardService.createCardForUser(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        CardResponseDto responseDto = (CardResponseDto) response.getBody();
        assertThat(responseDto.getCardNumber()).endsWith("3456");
        assertThat(responseDto.getBalance()).isEqualTo(new BigDecimal("100.00"));
    }

    @Test
    void getCardBalance_shouldReturnOk_whenCardBelongsToUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");

        Card card = new Card();
        card.setUser(user);
        card.setBalance(new BigDecimal("200.00"));

        when(encryptor.convertToDatabaseColumn(rawCardNumber)).thenReturn(encryptedCardNumber);
        when(cardRepository.findByCardNumberEncrypted(encryptedCardNumber)).thenReturn(Optional.of(card));
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        ResponseEntity<?> response = cardService.getCardBalance(rawCardNumber, "john");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<String, Object> responseMap = (Map<String, Object>) response.getBody();
        assertThat(responseMap.get("cardNumber").toString()).endsWith("3456");
        assertThat(responseMap.get("balance")).isEqualTo(new BigDecimal("200.00"));
    }

    @Test
    void blockCard_shouldReturnOk_whenUserOwnsCard() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");

        Card card = new Card();
        card.setUser(user);
        card.setStatus(CardStatus.ACTIVE);

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(encryptor.convertToDatabaseColumn(rawCardNumber)).thenReturn(encryptedCardNumber);
        when(cardRepository.findByCardNumberEncrypted(encryptedCardNumber)).thenReturn(Optional.of(card));

        ResponseEntity<?> response = cardService.blockCard(rawCardNumber, "john");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("The card was successfully blocked");
        assertThat(card.getStatus()).isEqualTo(CardStatus.BLOCKED);
    }

    @Test
    void transferBetweenOwnCards_shouldReturnOk_whenValid() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");

        Card fromCard = new Card();
        fromCard.setUser(user);
        fromCard.setStatus(CardStatus.ACTIVE);
        fromCard.setBalance(new BigDecimal("300.00"));

        Card toCard = new Card();
        toCard.setUser(user);
        toCard.setStatus(CardStatus.ACTIVE);
        toCard.setBalance(new BigDecimal("100.00"));

        TransferRequestDto dto = new TransferRequestDto();
        dto.setFromCardNumber(rawCardNumber);
        dto.setToCardNumber("9876543210987654");
        dto.setAmount(new BigDecimal("50.00"));

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(encryptor.convertToDatabaseColumn(rawCardNumber)).thenReturn("ENCRYPTED_FROM");
        when(encryptor.convertToDatabaseColumn("9876543210987654")).thenReturn("ENCRYPTED_TO");

        when(cardRepository.findByCardNumberEncrypted("ENCRYPTED_FROM")).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByCardNumberEncrypted("ENCRYPTED_TO")).thenReturn(Optional.of(toCard));

        ResponseEntity<?> response = cardService.transferBetweenOwnCards("john", dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Transfer completed successfully");
        assertThat(fromCard.getBalance()).isEqualTo(new BigDecimal("250.00"));
        assertThat(toCard.getBalance()).isEqualTo(new BigDecimal("150.00"));
    }

    @Test
    void getUserInfoByCardNumber_shouldReturnOk_whenCardExists() {
        User user = new User();
        user.setUsername("john");
        user.setEmail("john@example.com");

        Card card = new Card();
        card.setUser(user);
        card.setId(42L);
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(new BigDecimal("123.45"));

        when(encryptor.convertToDatabaseColumn(rawCardNumber)).thenReturn(encryptedCardNumber);
        when(cardRepository.findByCardNumberEncrypted(encryptedCardNumber)).thenReturn(Optional.of(card));

        ResponseEntity<?> response = cardService.getUserInfoByCardNumber(rawCardNumber);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserCardInfoDto dto = (UserCardInfoDto) response.getBody();
        assertThat(dto.getUserFullName()).isEqualTo("john");
        assertThat(dto.getUserEmail()).isEqualTo("john@example.com");
        assertThat(dto.getCardStatus()).isEqualTo(CardStatus.ACTIVE);
    }

    @Test
    void changeCardStatus_shouldReturnOk_whenStatusChanged() {
        Card card = new Card();
        card.setId(1L);
        card.setStatus(CardStatus.ACTIVE);
        User user = new User();
        user.setId(1L);
        card.setUser(user);

        ChangeCardStatusRequestDto dto = new ChangeCardStatusRequestDto();
        dto.setCardId(1L);
        dto.setUserId(1L);
        dto.setNewStatus(CardStatus.BLOCKED);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        ResponseEntity<?> response = cardService.changeCardStatus(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(card.getStatus()).isEqualTo(CardStatus.BLOCKED);
    }
}
