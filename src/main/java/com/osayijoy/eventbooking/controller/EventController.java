package com.osayijoy.eventbooking.controller;

import com.osayijoy.eventbooking.dto.request.EventRequestDTO;
import com.osayijoy.eventbooking.dto.response.EventResponseDto;
import com.osayijoy.eventbooking.service.EventService;
import com.osayijoy.eventbooking.utils.PaginatedResponseDTO;
import com.osayijoy.eventbooking.utils.response.ControllerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.osayijoy.eventbooking.utils.SwaggerDocUtil.*;
import static com.osayijoy.eventbooking.utils.constants.Constants.*;


/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */

@RestController
@RequestMapping(EVENT_API_VI)
@RequiredArgsConstructor
@Slf4j
@Tag(name = EVENT_CONTROLLER_SUMMARY, description = EVENT_CONTROLLER_DESCRIPTION)
public class EventController {
    private final EventService eventService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    @Operation(
            summary = CREATE_EVENT_SUMMARY,
            description = CREATE_EVENT_DESCRIPTION,
            tags = EVENT_CONTROLLER_SUMMARY,
            responses = {
                    @ApiResponse(
                            description = EVENT_CREATED,
                            responseCode = RESPONSE_CODE_201,
                            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = EventResponseDto.class))),
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
    public ResponseEntity<Object> createEvent(@RequestBody @Valid EventRequestDTO eventDto) {
        EventResponseDto createdEvent = eventService.createEvent(eventDto);
        return ControllerResponse.buildSuccessResponse(createdEvent, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Operation(
            summary = UPDATE_EVENT_SUMMARY,
            description = UPDATE_EVENT_DESCRIPTION,
            tags = EVENT_CONTROLLER_SUMMARY,
            responses = {
                    @ApiResponse(
                            description = EVENT_UPDATED,
                            responseCode = RESPONSE_CODE_200,
                            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = EventResponseDto.class))),
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
    public ResponseEntity<Object> updateEvent(@PathVariable Long id, @RequestBody @Valid EventRequestDTO eventDto) {
        EventResponseDto updatedEvent = eventService.updateEvent(id, eventDto);
        return ControllerResponse.buildSuccessResponse(updatedEvent);
    }

    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    @Operation(
            summary = GET_EVENT_BY_ID_SUMMARY,
            description = GET_EVENT_BY_ID_DESCRIPTION,
            tags = EVENT_CONTROLLER_SUMMARY,
            responses = {
                    @ApiResponse(
                            description = EVENT_FOUND,
                            responseCode = RESPONSE_CODE_200,
                            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = EventResponseDto.class))),
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
    public ResponseEntity<Object> getEventById(@PathVariable Long id) {
        EventResponseDto event = eventService.getEventById(id);
        return ControllerResponse.buildSuccessResponse(event);
    }

    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @Operation(
            summary = SEARCH_EVENTS_SUMMARY,
            description = SEARCH_EVENTS_DESCRIPTION,
            tags = EVENT_CONTROLLER_SUMMARY,
            responses = {
                    @ApiResponse(
                            description = EVENTS_FOUND,
                            responseCode = RESPONSE_CODE_200,
                            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = PaginatedResponseDTO.class))),
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
    public ResponseEntity<Object> searchEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = PAGE_NUMBER_DEFAULT_VALUE, required = false) int page,
            @RequestParam(defaultValue = PAGE_SIZE_DEFAULT_VALUE, required = false) int size) {
        PaginatedResponseDTO<EventResponseDto> events = eventService.searchEvents(name, startDate, endDate, category, page, size);
        return ControllerResponse.buildSuccessResponse(events);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(
            summary = DELETE_EVENT_SUMMARY,
            description = DELETE_EVENT_DESCRIPTION,
            tags = EVENT_CONTROLLER_SUMMARY,
            responses = {
                    @ApiResponse(
                            description = EVENT_DELETED,
                            responseCode = RESPONSE_CODE_200,
                            content = @Content(mediaType = APPLICATION_JSON)),
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
    public ResponseEntity<Object> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ControllerResponse.buildSuccessResponse();
    }

}
