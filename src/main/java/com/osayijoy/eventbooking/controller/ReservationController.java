package com.osayijoy.eventbooking.controller;

import com.osayijoy.eventbooking.dto.ReservationDto;
import com.osayijoy.eventbooking.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Joy Osayi
 * @createdOn Jun-29(Sat)-2024
 */

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reservation Management", description = "Endpoints for managing reservations")
@ApiResponses({
        @ApiResponse(description = "Success", responseCode = "200", content = @Content),
        @ApiResponse(description = "Created", responseCode = "201", content = @Content),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
})
public class ReservationController {
    private final ReservationService reservationService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping
    @Operation(
            summary = "Create Reservation",
            description = "Create a new reservation for an event",
            tags = "Reservation Management",
            responses = {
                    @ApiResponse(
                            description = "Reservation Created",
                            responseCode = "201",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReservationDto.class))
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<ReservationDto> createReservation(
            @RequestParam Long eventId,
            @RequestParam Long userId,
            @RequestParam int attendeesCount) {
        ReservationDto createdReservation = reservationService.createReservation(eventId, userId, attendeesCount);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Get Reservations by User",
            description = "Retrieve reservations made by a specific user",
            tags = "Reservation Management",
            responses = {
                    @ApiResponse(
                            description = "Reservations Found",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReservationDto.class))
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<List<ReservationDto>> getReservationsByUser(@PathVariable Long userId) {
        List<ReservationDto> reservations = reservationService.getReservationsByUser(userId);
        return ResponseEntity.ok(reservations);
    }

    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/event/{eventId}")
    @Operation(
            summary = "Get Reservations by Event",
            description = "Retrieve reservations made for a specific event",
            tags = "Reservation Management",
            responses = {
                    @ApiResponse(
                            description = "Reservations Found",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReservationDto.class))
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<List<ReservationDto>> getReservationsByEvent(@PathVariable Long eventId) {
        List<ReservationDto> reservations = reservationService.getReservationsByEvent(eventId);
        return ResponseEntity.ok(reservations);
    }

    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{reservationId}")
    @Operation(
            summary = "Cancel Reservation",
            description = "Cancel an existing reservation",
            tags = "Reservation Management",
            responses = {
                    @ApiResponse(
                            description = "Reservation Cancelled",
                            responseCode = "204",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.noContent().build();
    }
}

