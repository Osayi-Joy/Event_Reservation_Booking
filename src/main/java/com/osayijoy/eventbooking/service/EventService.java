package com.osayijoy.eventbooking.service;

import com.osayijoy.eventbooking.dto.EventDto;
import com.osayijoy.eventbooking.dto.request.EventRequestDTO;
import com.osayijoy.eventbooking.dto.response.EventResponseDto;

import java.util.List;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */

public interface EventService {
    EventResponseDto createEvent(EventRequestDTO eventDto);
    EventResponseDto updateEvent(Long id, EventRequestDTO eventDto);
    EventResponseDto getEventById(Long id);
    List<EventResponseDto> getAllEvents();
    List<EventResponseDto> searchEvents(String name, String category);
    void deleteEvent(Long id);
}
