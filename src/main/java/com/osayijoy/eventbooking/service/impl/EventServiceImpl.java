package com.osayijoy.eventbooking.service.impl;

import com.osayijoy.eventbooking.dto.request.EventRequestDTO;
import com.osayijoy.eventbooking.dto.response.EventResponseDto;
import com.osayijoy.eventbooking.exception.ResourceNotFoundException;
import com.osayijoy.eventbooking.model.Event;
import com.osayijoy.eventbooking.repository.EventRepository;
import com.osayijoy.eventbooking.service.EventService;
import com.osayijoy.eventbooking.utils.BeanUtilWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {


    private final EventRepository eventRepository;

    @Override
    @Transactional
    public EventResponseDto createEvent(EventRequestDTO eventDto) {
        Event event = mapToEntity(eventDto);
        event = eventRepository.save(event);
        return mapToDto(event);
    }

    @Transactional
    public EventResponseDto updateEvent(Long id, EventRequestDTO eventDto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));

        event.setName(eventDto.getName());
        event.setDate(eventDto.getDate());
        event.setAvailableAttendeesCount(eventDto.getAvailableAttendeesCount());
        event.setDescription(eventDto.getDescription());
        event.setCategory(eventDto.getCategory());
        event = eventRepository.save(event);

        return mapToDto(event);
    }

    @Override
    public EventResponseDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));
        return mapToDto(event);
    }

    @Override
    public List<EventResponseDto> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponseDto> searchEvents(String name, String category) {
        List<Event> events = eventRepository.findByNameContainingAndCategory(name, category);
        return events.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));
        eventRepository.delete(event);
    }

    private EventResponseDto mapToDto(Event event) {
        EventResponseDto eventResponseDto = new EventResponseDto();
        BeanUtilWrapper.copyNonNullProperties(event, eventResponseDto);
        return eventResponseDto;
    }
    private Event mapToEntity(EventRequestDTO eventRequestDTO) {
        Event event = new Event();
        BeanUtilWrapper.copyNonNullProperties(eventRequestDTO, event);
        return event;
    }
}
