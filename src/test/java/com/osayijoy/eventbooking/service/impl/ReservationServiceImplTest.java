package com.osayijoy.eventbooking.service.impl;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @author Joy Osayi
 * @createdOn Jun-30(Sun)-2024
 */
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.osayijoy.eventbooking.dto.ReservationDto;
import com.osayijoy.eventbooking.enums.Category;
import com.osayijoy.eventbooking.exception.InsufficientTicketsException;
import com.osayijoy.eventbooking.exception.ResourceNotFoundException;
import com.osayijoy.eventbooking.model.Event;
import com.osayijoy.eventbooking.model.Reservation;
import com.osayijoy.eventbooking.model.User;
import com.osayijoy.eventbooking.repository.EventRepository;
import com.osayijoy.eventbooking.repository.ReservationRepository;
import com.osayijoy.eventbooking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Event event;
    private User user;
    private Reservation reservation;
    private ReservationDto reservationDto;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setCategory(Category.CONFERENCE);
        event.setDate(LocalDate.now());
        event.setAvailableAttendeesCount(100);
        event.setDescription("Test Description");

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setEvent(event);
        reservation.setUser(user);
        reservation.setAttendeesCount(2);

        reservationDto = new ReservationDto();
        reservationDto.setId(1L);
        reservationDto.setEventId(event.getId());
        reservationDto.setUserId(user.getId());
        reservationDto.setAttendeesCount(2);
    }

    @Test
    void testCreateReservation() {
        when(eventRepository.findById(eq(1L))).thenReturn(Optional.of(event));
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        ReservationDto result = reservationService.createReservation(1L, 1L, 2);

        assertThat(result.getEventId()).isEqualTo(1L);
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getAttendeesCount()).isEqualTo(2);
        verify(eventRepository, times(1)).findById(eq(1L));
        verify(userRepository, times(1)).findById(eq(1L));
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testCreateReservation_InsufficientTickets() {
        event.setAvailableAttendeesCount(1);

        when(eventRepository.findById(eq(1L))).thenReturn(Optional.of(event));
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));

        assertThrows(InsufficientTicketsException.class, () -> reservationService.createReservation(1L, 1L, 2));
        verify(eventRepository, times(1)).findById(eq(1L));
        verify(userRepository, times(1)).findById(eq(1L));
    }

    @Test
    void testGetReservationsByUser() {
        when(reservationRepository.findByUserId(eq(1L))).thenReturn(Arrays.asList(reservation));

        List<ReservationDto> result = reservationService.getReservationsByUser(1L);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getEventId()).isEqualTo(1L);
        assertThat(result.get(0).getUserId()).isEqualTo(1L);
        verify(reservationRepository, times(1)).findByUserId(eq(1L));
    }

    @Test
    void testGetReservationsByEvent() {
        when(reservationRepository.findByEventId(eq(1L))).thenReturn(Arrays.asList(reservation));

        List<ReservationDto> result = reservationService.getReservationsByEvent(1L);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getEventId()).isEqualTo(1L);
        assertThat(result.get(0).getUserId()).isEqualTo(1L);
        verify(reservationRepository, times(1)).findByEventId(eq(1L));
    }

    @Test
    void testCancelReservation() {
        when(reservationRepository.findById(eq(1L))).thenReturn(Optional.of(reservation));

        reservationService.cancelReservation(1L);

        assertThat(event.getAvailableAttendeesCount()).isEqualTo(102);
        verify(eventRepository, times(1)).save(any(Event.class));
        verify(reservationRepository, times(1)).findById(eq(1L));
        verify(reservationRepository, times(1)).delete(any(Reservation.class));
    }

    @Test
    void testCancelReservation_NotFound() {
        when(reservationRepository.findById(eq(1L))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.cancelReservation(1L));
        verify(reservationRepository, times(1)).findById(eq(1L));
    }
}

