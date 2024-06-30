package com.osayijoy.eventbooking.service;

import com.osayijoy.eventbooking.dto.request.EventRequestDTO;
import com.osayijoy.eventbooking.dto.response.EventResponseDto;
import com.osayijoy.eventbooking.utils.PaginatedResponseDTO;

import java.util.List;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */

public interface EventService {
    EventResponseDto createEvent(EventRequestDTO eventDto);
    EventResponseDto updateEvent(Long id, EventRequestDTO eventDto);
    EventResponseDto getEventById(Long id);
    PaginatedResponseDTO<EventResponseDto> searchEvents(String name,
                                                        String startDate,
                                                        String endDate,
                                                        String category,
                                                        int page, int size);
    void deleteEvent(Long id);

    List<EventResponseDto> getUpcomingEvents();

    void notifyUsersOfUpcomingEvent(Long eventId);
}
