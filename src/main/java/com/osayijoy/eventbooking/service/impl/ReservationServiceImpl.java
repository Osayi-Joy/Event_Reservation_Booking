package com.osayijoy.eventbooking.service.impl;
/**
 * @author Joy Osayi
 * @createdOn Jun-28(Fri)-2024
 */
import com.osayijoy.eventbooking.dto.ReservationDto;
import com.osayijoy.eventbooking.dto.request.EventRequestDTO;
import com.osayijoy.eventbooking.exception.InsufficientTicketsException;
import com.osayijoy.eventbooking.exception.ResourceNotFoundException;
import com.osayijoy.eventbooking.model.Event;
import com.osayijoy.eventbooking.model.Reservation;
import com.osayijoy.eventbooking.model.User;
import com.osayijoy.eventbooking.repository.EventRepository;
import com.osayijoy.eventbooking.repository.ReservationRepository;
import com.osayijoy.eventbooking.repository.UserRepository;
import com.osayijoy.eventbooking.service.ReservationService;
import com.osayijoy.eventbooking.utils.BeanUtilWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {


    private final ReservationRepository reservationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ReservationDto createReservation(Long eventId, Long userId, int attendeesCount) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + eventId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        if (event.getAvailableAttendeesCount() < attendeesCount) {
            throw new InsufficientTicketsException("Not enough tickets available for event " + eventId);
        }

        Reservation reservation = new Reservation();
        reservation.setEvent(event);
        reservation.setUser(user);
        reservation.setAttendeesCount(attendeesCount);

        event.setAvailableAttendeesCount(event.getAvailableAttendeesCount() - attendeesCount);
        eventRepository.save(event);

        reservation = reservationRepository.save(reservation);
        return mapToDto(reservation);
    }

    @Override
    public List<ReservationDto> getReservationsByUser(Long userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        return reservations.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationDto> getReservationsByEvent(Long eventId) {
        List<Reservation> reservations = reservationRepository.findByEventId(eventId);
        return reservations.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + reservationId));

        Event event = reservation.getEvent();
        event.setAvailableAttendeesCount(event.getAvailableAttendeesCount() + reservation.getAttendeesCount());
        eventRepository.save(event);

        reservationRepository.delete(reservation);
    }

    private ReservationDto mapToDto(Reservation reservation) {
        ReservationDto reservationDto = new ReservationDto();
        BeanUtilWrapper.copyNonNullProperties(reservation, reservationDto);
        return reservationDto;
    }

}

