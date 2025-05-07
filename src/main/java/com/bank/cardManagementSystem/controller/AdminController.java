package com.bank.cardManagementSystem.controller;

import com.bank.cardManagementSystem.dto.UserCardInfoDto;
import com.bank.cardManagementSystem.dto.request.CardNumberRequestDto;
import com.bank.cardManagementSystem.dto.request.ChangeCardStatusRequestDto;
import com.bank.cardManagementSystem.dto.CreateCardDto;
import com.bank.cardManagementSystem.dto.response.CardResponseDto;
import com.bank.cardManagementSystem.service.impl.CardServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admins")
@Tag(name = "Admin Operations", description = "Operations for Admins, including card management.")
public class AdminController {

    private final CardServiceImpl cardService;

    @Operation(
            summary = "Create a new card for a user",
            description = "This endpoint allows the admin to create a new card for a specified user. " +
                    "It requires the user's ID, the card's number, expiration date, and initial balance. " +
                    "The card is encrypted, validated, and then saved to the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Card created successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CardResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data or card already exists"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            },
            parameters = {
                    @Parameter(name = "CreateCardDto", description = "Request body containing card creation details", required = true)
            }
    )
    @PostMapping("/create/cards")
    public ResponseEntity<?> createCard(@Valid @RequestBody CreateCardDto request) {
        return cardService.createCardForUser(request);
    }

    @Operation(
            summary = "Get card owner info by card number",
            description = "Returns information about the user who owns the specified card number"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User info returned successfully",
                    content = @Content(schema = @Schema(implementation = UserCardInfoDto.class))),
            @ApiResponse(responseCode = "404", description = "Card or user not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping("/card/info")
    public ResponseEntity<?> getCardUserInfo(@Valid @RequestBody CardNumberRequestDto request) {
        return cardService.getUserInfoByCardNumber(request.getCardNumber());
    }

    @Operation(
            summary = "Change the status of a card",
            description = "This endpoint allows you to change the status of a card. The request requires the card ID, the user ID, " +
                    "and the new status. It also performs validation to ensure the card exists, belongs to the user, and " +
                    "the status is different from the current one.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Card status successfully changed",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Card not found or user is not the owner of the card"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            },
            parameters = {
                    @Parameter(name = "cardId", description = "The ID of the card whose status needs to be changed", required = true),
                    @Parameter(name = "userId", description = "The ID of the user performing the action", required = true),
                    @Parameter(name = "newStatus", description = "The new status to which the card should be changed", required = true)
            }
    )
    @PostMapping("/change-status")
    public ResponseEntity<?> changeCardStatus(@Valid @RequestBody ChangeCardStatusRequestDto request) {
        return cardService.changeCardStatus(request);
    }

    @Operation(
            summary = "Delete a card by ID",
            description = "This endpoint allows the admin to delete a card by its unique card ID. " +
                    "If the card with the provided ID is found, it will be deleted from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Card deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Card not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            },
            parameters = {
                    @Parameter(name = "cardId", description = "The ID of the card to be deleted", required = true)
            }
    )
    @DeleteMapping("/card/delete/{cardId}")
    public ResponseEntity<?> deleteUserCard(@PathVariable Long cardId) {
        return cardService.deleteCardById(cardId);
    }

}
