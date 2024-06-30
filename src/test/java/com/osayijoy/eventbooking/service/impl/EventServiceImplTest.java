package com.osayijoy.eventbooking.service.impl;
/**
 * @author Joy Osayi
 * @createdOn Jun-30(Sun)-2024
 */
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.osayijoy.eventbooking.dto.request.EventRequestDTO;
import com.osayijoy.eventbooking.dto.response.EventResponseDto;
import com.osayijoy.eventbooking.enums.Category;
import com.osayijoy.eventbooking.exception.ResourceNotFoundException;
import com.osayijoy.eventbooking.model.Event;
import com.osayijoy.eventbooking.repository.EventRepository;
import com.osayijoy.eventbooking.utils.PaginatedResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    private Event event;
    private EventRequestDTO eventRequestDTO;
    private EventResponseDto eventResponseDto;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setCategory(Category.CONFERENCE);
        event.setDate(LocalDateTime.now());
        event.setAvailableAttendeesCount(100);
        event.setDescription("Test Description");

        eventRequestDTO = new EventRequestDTO();
        eventRequestDTO.setName("Test Event");
        eventRequestDTO.setCategory(Category.CONFERENCE);
        eventRequestDTO.setDate(LocalDateTime.now());
        eventRequestDTO.setAvailableAttendeesCount(100);
        eventRequestDTO.setDescription("Test Description");

        eventResponseDto = new EventResponseDto();
        eventResponseDto.setId(1L);
        eventResponseDto.setName("Test Event");
        eventResponseDto.setCategory(Category.CONCERT);
        eventResponseDto.setDate(LocalDate.now());
        eventResponseDto.setAvailableAttendeesCount(100);
        eventResponseDto.setDescription("Test Description");
    }

    @Test
    void testCreateEvent() {
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventResponseDto result = eventService.createEvent(eventRequestDTO);

        assertThat(result.getName()).isEqualTo(eventRequestDTO.getName());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void testUpdateEvent() {
        when(eventRepository.findById(eq(1L))).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventResponseDto result = eventService.updateEvent(1L, eventRequestDTO);

        assertThat(result.getName()).isEqualTo(eventRequestDTO.getName());
        verify(eventRepository, times(1)).findById(eq(1L));
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void testGetEventById() {
        when(eventRepository.findById(eq(1L))).thenReturn(Optional.of(event));

        EventResponseDto result = eventService.getEventById(1L);

        assertThat(result.getName()).isEqualTo(event.getName());
        verify(eventRepository, times(1)).findById(eq(1L));
    }

    @Test
    void testSearchEvents() {
        List<Event> events = Arrays.asList(event);
        Page<Event> eventPage = new PageImpl<>(events);
        when(eventRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(eventPage);

        PaginatedResponseDTO<EventResponseDto> result = eventService.searchEvents(
                "Test Event", null, null, "Conference", 0, 10);

        assertThat(result.getContent().size()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Test Event");
        verify(eventRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void testDeleteEvent() {
        when(eventRepository.findById(eq(1L))).thenReturn(Optional.of(event));
        doNothing().when(eventRepository).delete(any(Event.class));

        eventService.deleteEvent(1L);

        verify(eventRepository, times(1)).findById(eq(1L));
        verify(eventRepository, times(1)).delete(any(Event.class));
    }

    @Test
    void testEventNotFound() {
        when(eventRepository.findById(eq(1L))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventService.getEventById(1L));
        verify(eventRepository, times(1)).findById(eq(1L));
    }

    @Test
    public void testGetUpcomingEvents() {
        Event event1 = new Event();
        event1.setName("Event 1");
        event1.setDate(LocalDateTime.now().plusHours(1));

        Event event2 = new Event();
        event2.setName("Event 2");
        event2.setDate(LocalDateTime.now().plusHours(2));

        when(eventRepository.findByDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(event1, event2));

        List<EventResponseDto> upcomingEvents = eventService.getUpcomingEvents();

        assertThat(upcomingEvents.size()).isEqualTo(2);
        assertThat(upcomingEvents.get(0).getName()).isEqualTo("Event 1");
        assertThat(upcomingEvents.get(1).getName()).isEqualTo("Event 2");
    }
}

