package com.bank.cardManagementSystem.controller;

import com.bank.cardManagementSystem.dto.request.TransferRequestDto;
import com.bank.cardManagementSystem.service.impl.CardServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Tag(name = "User Operations", description = "Operations for user, including card management.")
public class UserController {

    private final CardServiceImpl cardService;

    @Operation(
            summary = "Get balance of a specific card",
            description = "Returns the balance for a specific card number if the card belongs to the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance returned successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - card does not belong to user"),
            @ApiResponse(responseCode = "404", description = "Card not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/cards/{cardNumber}/balance")
    public ResponseEntity<?> getCardBalance(@PathVariable String cardNumber, Principal principal) {
        return cardService.getCardBalance(cardNumber, principal.getName());
    }

    @Operation(
            summary = "Get all cards of the current user",
            description = "Returns a list of all cards belonging to the authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cards retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - user not authenticated"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/cards")
    public ResponseEntity<?> getAllMyCards(Principal principal) {
        return cardService.getAllCardsByUsername(principal.getName());
    }

    @Operation(
            summary = "Block user card",
            description = "Blocks the specified card by its number, belonging to the currently authenticated user. " +
                    "The card number must be provided as a request parameter, e.g., " +
                    "`/user/card/block?cardNumber=3424567812345623`.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card successfully blocked"),
            @ApiResponse(responseCode = "400", description = "The card does not belong to the user or is invalid", content = @Content),
            @ApiResponse(responseCode = "404", description = "User or card not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/card/block")
    public ResponseEntity<?> requestCardBlock(@RequestParam String cardNumber, Principal principal) {
        return cardService.blockCard(cardNumber, principal.getName());
    }

    @Operation(
            summary = "Transfer money between user's own cards",
            description = "Transfers a specified amount from one card to another, both of which must belong to the authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer completed successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed or insufficient funds", content = @Content),
            @ApiResponse(responseCode = "404", description = "Card not found or not owned by the user", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/cards/transfer")
    public ResponseEntity<?> transferBetweenCards(@Valid @RequestBody TransferRequestDto dto, Principal principal) {
        return cardService.transferBetweenOwnCards(principal.getName(), dto);
    }

}
