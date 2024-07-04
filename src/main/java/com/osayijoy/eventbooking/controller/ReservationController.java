package com.osayijoy.eventbooking.controller;

import com.osayijoy.eventbooking.dto.ReservationDto;
import com.osayijoy.eventbooking.service.ReservationService;
import com.osayijoy.eventbooking.utils.PaginatedResponseDTO;
import com.osayijoy.eventbooking.utils.response.ControllerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static com.osayijoy.eventbooking.utils.SwaggerDocUtil.*;
import static com.osayijoy.eventbooking.utils.constants.Constants.*;

/**
 * @author Joy Osayi
 * @createdOn Jun-29(Sat)-2024
 */

@RestController
@RequestMapping(RESERVATION_API_V1)
@RequiredArgsConstructor
@Slf4j
public class ReservationController {
    private final ReservationService reservationService;

    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @PostMapping
    @Operation(
            summary = CREATE_RESERVATION_SUMMARY,
            description = CREATE_RESERVATION_DESCRIPTION,
            tags = RESERVATION_MANAGEMENT_TAG,
            responses = {
                    @ApiResponse(
                            description = RESERVATION_CREATED,
                            responseCode = RESPONSE_CODE_201,
                            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ReservationDto.class))),
                    @ApiResponse(
                            description = BAD_REQUEST,
                            responseCode = RESPONSE_CODE_400,
                            content = @Content(mediaType = APPLICATION_JSON)),
                    @ApiResponse(
                            description = UNAUTHORIZED,
                            responseCode = RESPONSE_CODE_401,
                            content = @Content(mediaType = APPLICATION_JSON))
            }
    )
    public ResponseEntity<Object> createReservation(
            @RequestParam Long eventId,
            @RequestParam String email,
            @RequestParam int attendeesCount) {
        ReservationDto createdReservation = reservationService.createReservation(eventId, email, attendeesCount);
        return ControllerResponse.buildSuccessResponse(createdReservation, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/user/{email}")
    @Operation(
            summary = GET_RESERVATIONS_BY_USER_SUMMARY,
            description = GET_RESERVATIONS_BY_USER_DESCRIPTION,
            tags = RESERVATION_MANAGEMENT_TAG,
            responses = {
                    @ApiResponse(
                            description = RESERVATIONS_FOUND,
                            responseCode = RESPONSE_CODE_200,
                            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ReservationDto.class))),
                    @ApiResponse(
                            description = NOT_FOUND,
                            responseCode = RESPONSE_CODE_404,
                            content = @Content(mediaType = APPLICATION_JSON)),
                    @ApiResponse(
                            description = UNAUTHORIZED,
                            responseCode = RESPONSE_CODE_401,
                            content = @Content(mediaType = APPLICATION_JSON))
            }
    )
    public ResponseEntity<Object> getReservationsByUser(@PathVariable String email,
           @RequestParam(defaultValue = PAGE_NUMBER_DEFAULT_VALUE, required = false) int page,
           @RequestParam(defaultValue = PAGE_SIZE_DEFAULT_VALUE, required = false) int size) {
        PaginatedResponseDTO<ReservationDto> reservations = reservationService.getReservationsByUser(email, page, size);
        return ControllerResponse.buildSuccessResponse(reservations);
    }

    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/event/{eventId}")
    @Operation(
            summary = GET_RESERVATIONS_BY_EVENT_SUMMARY,
            description = GET_RESERVATIONS_BY_EVENT_DESCRIPTION,
            tags = RESERVATION_MANAGEMENT_TAG,
            responses = {
                    @ApiResponse(
                            description = RESERVATIONS_FOUND,
                            responseCode = RESPONSE_CODE_200,
                            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ReservationDto.class))),
                    @ApiResponse(
                            description = NOT_FOUND,
                            responseCode = RESPONSE_CODE_404,
                            content = @Content(mediaType = APPLICATION_JSON)),
                    @ApiResponse(
                            description = UNAUTHORIZED,
                            responseCode = RESPONSE_CODE_401,
                            content = @Content(mediaType = APPLICATION_JSON))
            }
    )
    public ResponseEntity<Object> getReservationsByEvent(@PathVariable Long eventId,
           @RequestParam(defaultValue = PAGE_NUMBER_DEFAULT_VALUE, required = false) int page,
           @RequestParam(defaultValue = PAGE_SIZE_DEFAULT_VALUE, required = false) int size) {
        PaginatedResponseDTO<ReservationDto> reservations = reservationService.getReservationsByEvent(eventId, page, size);
        return ControllerResponse.buildSuccessResponse(reservations);
    }

    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/event/{eventId}/{email}")
    @Operation(
            summary = GET_RESERVATIONS_BY_EVENT_SUMMARY,
            description = GET_RESERVATIONS_BY_EVENT_DESCRIPTION,
            tags = RESERVATION_MANAGEMENT_TAG,
            responses = {
                    @ApiResponse(
                            description = RESERVATIONS_FOUND,
                            responseCode = RESPONSE_CODE_200,
                            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ReservationDto.class))),
                    @ApiResponse(
                            description = NOT_FOUND,
                            responseCode = RESPONSE_CODE_404,
                            content = @Content(mediaType = APPLICATION_JSON)),
                    @ApiResponse(
                            description = UNAUTHORIZED,
                            responseCode = RESPONSE_CODE_401,
                            content = @Content(mediaType = APPLICATION_JSON))
            }
    )
    public ResponseEntity<Object> getReservation(@PathVariable Long eventId, @PathVariable String email){
              ReservationDto reservation = reservationService.getAReservation(eventId, email);
        return ControllerResponse.buildSuccessResponse(reservation);
    }

    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{reservationId}")
    @Operation(
            summary = CANCEL_RESERVATION_SUMMARY,
            description = CANCEL_RESERVATION_DESCRIPTION,
            tags = RESERVATION_MANAGEMENT_TAG,
            responses = {
                    @ApiResponse(
                            description = RESERVATION_CANCELLED,
                            responseCode = RESPONSE_CODE_204,
                            content = @Content),
                    @ApiResponse(
                            description = NOT_FOUND,
                            responseCode = RESPONSE_CODE_404,
                            content = @Content(mediaType = APPLICATION_JSON)),
                    @ApiResponse(
                            description = UNAUTHORIZED,
                            responseCode = RESPONSE_CODE_401,
                            content = @Content(mediaType = APPLICATION_JSON))
            }
    )
    public ResponseEntity<Object> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ControllerResponse.buildSuccessResponse();
    }
}

