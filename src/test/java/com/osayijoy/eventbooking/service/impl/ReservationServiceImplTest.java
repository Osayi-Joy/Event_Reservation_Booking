package com.osayijoy.eventbooking.service.impl;

/**
 * @author Joy Osayi
 * @createdOn Jun-30(Sun)-2024
 */
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
import com.osayijoy.eventbooking.utils.PaginatedResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
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
    private String email;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setCategory(Category.CONFERENCE);
        event.setDate(LocalDateTime.now());
        event.setAvailableAttendeesCount(100);
        event.setDescription("Test Description");

        email = "osayijoy17@gmail.com";
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail(email);

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setEvent(event);
        reservation.setUser(user);
        reservation.setAttendeesCount(2);

        reservationDto = new ReservationDto();
        reservationDto.setId(1L);
        reservationDto.setEventName(event.getName());
        reservationDto.setEventId(event.getId());
        reservationDto.setUserEmail(user.getEmail());
        reservationDto.setAttendeesCount(2);
    }

    @Test
    void testCreateReservation() {
        when(eventRepository.findById(eq(1L))).thenReturn(Optional.of(event));
        when(userRepository.findFirstByEmailOrderByCreatedDate(eq(email))).thenReturn(Optional.of(user));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        ReservationDto result = reservationService.createReservation(1L, email, 2);

        assertThat(result.getEventId()).isEqualTo(1L);
        assertThat(result.getUserEmail()).isEqualTo(email);
        assertThat(result.getAttendeesCount()).isEqualTo(2);
        verify(eventRepository, times(1)).findById(eq(1L));
        verify(userRepository, times(1)).findFirstByEmailOrderByCreatedDate(eq(email));
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testCreateReservation_InsufficientTickets() {
        event.setAvailableAttendeesCount(1);

        when(eventRepository.findById(eq(1L))).thenReturn(Optional.of(event));
        when(userRepository.findFirstByEmailOrderByCreatedDate(eq(email))).thenReturn(Optional.of(user));

        assertThrows(InsufficientTicketsException.class, () -> reservationService.createReservation(1L, email, 2));
        verify(eventRepository, times(1)).findById(eq(1L));
        verify(userRepository, times(1)).findFirstByEmailOrderByCreatedDate(eq(email));
    }

    @Test
    void testGetReservationsByUser() {
        when(reservationRepository.findByUserEmail(eq(email), any(Pageable.class))).thenReturn(createPage(reservation));

        PaginatedResponseDTO<ReservationDto> result = reservationService.getReservationsByUser(email, 0, 10);

        assertThat(result.getContent().size()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEventId()).isEqualTo(1L);
        assertThat(result.getContent().get(0).getUserEmail()).isEqualTo(email);
        verify(reservationRepository, times(1)).findByUserEmail(eq(email), any(Pageable.class));
    }

    @Test
    void testGetReservationsByEvent() {
        when(reservationRepository.findByEventId(eq(1L), any(Pageable.class))).thenReturn(createPage(reservation));

        PaginatedResponseDTO<ReservationDto> result = reservationService.getReservationsByEvent(1L, 0, 10);

        assertThat(result.getContent().size()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEventId()).isEqualTo(1L);
        assertThat(result.getContent().get(0).getUserEmail()).isEqualTo(email);
        verify(reservationRepository, times(1)).findByEventId(eq(1L), any(Pageable.class));
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
    void testGetAReservation_Found() {
        when(reservationRepository.findByEventIdAndUserEmail(1L, user.getEmail()))
                .thenReturn(Optional.of(reservation));

        ReservationDto result = reservationService.getAReservation(1L, user.getEmail());

        assertEquals(reservationDto.getEventName(), result.getEventName());
        verify(reservationRepository, times(1)).findByEventIdAndUserEmail(1L, user.getEmail());
    }

    @Test
    void testGetAReservation_NotFound() {
        when(reservationRepository.findByEventIdAndUserEmail(1L, user.getEmail()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            reservationService.getAReservation(1L, user.getEmail());
        });

        verify(reservationRepository, times(1)).findByEventIdAndUserEmail(1L, user.getEmail());
    }
    @Test
    void testCancelReservation_NotFound() {
        when(reservationRepository.findById(eq(1L))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.cancelReservation(1L));
        verify(reservationRepository, times(1)).findById(eq(1L));
    }
    @Test
    public void testGetUserEmailsForEvent() {
        User user1 = new User();
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setEmail("user2@example.com");

        Reservation reservation1 = new Reservation();
        reservation1.setUser(user1);

        Reservation reservation2 = new Reservation();
        reservation2.setUser(user2);

        when(reservationRepository.findByEventId(1L)).thenReturn(Arrays.asList(reservation1, reservation2));

        List<String> userEmails = reservationService.getUserEmailsForEvent(1L);

        assertThat(userEmails).containsExactlyInAnyOrder("user1@example.com", "user2@example.com");
    }

    private static Page<Reservation> createPage(Reservation reservation) {
        List<Reservation> reservations = Collections.singletonList(reservation);
        return new PageImpl<>(reservations, PageRequest.of(0, 10), reservations.size());
    }
}

