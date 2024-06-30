package com.osayijoy.eventbooking.controller;

import com.osayijoy.eventbooking.dto.request.EventRequestDTO;
import com.osayijoy.eventbooking.dto.response.EventResponseDto;
import com.osayijoy.eventbooking.service.EventService;
import com.osayijoy.eventbooking.utils.PaginatedResponseDTO;
import com.osayijoy.eventbooking.utils.response.ControllerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Event Management", description = "Endpoints for managing events")
@ApiResponses({
        @ApiResponse(description = "Success", responseCode = "200", content = @Content),
        @ApiResponse(description = "Created", responseCode = "201", content = @Content),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
})
public class EventController {
    private final EventService eventService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    @Operation(summary = "Create Event", description = "Create a new event")
    public ResponseEntity<Object> createEvent(@RequestBody @Valid EventRequestDTO eventDto) {
        EventResponseDto createdEvent = eventService.createEvent(eventDto);
        return ControllerResponse.buildSuccessResponse(createdEvent);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update Event", description = "Update an existing event")
    public ResponseEntity<Object> updateEvent(@PathVariable Long id, @RequestBody @Valid EventRequestDTO eventDto) {
        EventResponseDto updatedEvent = eventService.updateEvent(id, eventDto);
        return ControllerResponse.buildSuccessResponse(updatedEvent);
    }

    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Get Event by ID", description = "Retrieve an event by its ID")
    public ResponseEntity<Object> getEventById(@PathVariable Long id) {
        EventResponseDto event = eventService.getEventById(id);
        return ControllerResponse.buildSuccessResponse(event);
    }

    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @Operation(summary = "Search or Retrieve All Events", description = "Retrieve events optionally filtered by name, date range, or category")
    public ResponseEntity<Object> searchEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size) {
        PaginatedResponseDTO<EventResponseDto> events = eventService.searchEvents(name, startDate, endDate, category, page, size);
        return ControllerResponse.buildSuccessResponse(events);
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Event", description = "Delete an event by its ID")
    public ResponseEntity<Object> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ControllerResponse.buildSuccessResponse();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/{eventId}/notify-users")
    @Operation(summary = "Notify Users of Upcoming Event", description = "Send notification to users about an upcoming event")
    public ResponseEntity<Void> notifyUsersOfUpcomingEvent(@PathVariable Long eventId) {
        eventService.notifyUsersOfUpcomingEvent(eventId);
        return ResponseEntity.ok().build();
    }

}
