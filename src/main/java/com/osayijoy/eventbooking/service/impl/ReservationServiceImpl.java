package com.osayijoy.eventbooking.service.impl;
/**
 * @author Joy Osayi
 * @createdOn Jun-28(Fri)-2024
 */
import com.osayijoy.eventbooking.dto.ReservationDto;
import com.osayijoy.eventbooking.exception.InsufficientTicketsException;
import com.osayijoy.eventbooking.exception.ResourceNotFoundException;
import com.osayijoy.eventbooking.model.Event;
import com.osayijoy.eventbooking.model.Reservation;
import com.osayijoy.eventbooking.model.User;
import com.osayijoy.eventbooking.repository.EventRepository;
import com.osayijoy.eventbooking.repository.ReservationRepository;
import com.osayijoy.eventbooking.repository.UserRepository;
import com.osayijoy.eventbooking.service.ReservationService;
import com.osayijoy.eventbooking.utils.PaginatedResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ReservationDto createReservation(Long eventId, String email, int attendeesCount) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + eventId));

        User user = userRepository.findFirstByEmailOrderByCreatedDate(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + email));

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
    public PaginatedResponseDTO<ReservationDto>getReservationsByUser(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Reservation> reservations = reservationRepository.findByUserEmail(email, pageable);
        var response = reservations.stream()
                .map(this::mapToDto)
                .toList();
        return PaginatedResponseDTO.<ReservationDto>builder()
                .content(response)
                .currentPage(reservations.getNumber())
                .totalPages(reservations.getTotalPages())
                .totalItems(reservations.getTotalElements())
                .isFirstPage(reservations.isFirst())
                .isLastPage(reservations.isLast())
                .build();
    }

    @Override
    public PaginatedResponseDTO<ReservationDto> getReservationsByEvent(Long eventId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Reservation> reservations = reservationRepository.findByEventId(eventId, pageable);
        var response = reservations.stream()
                .map(this::mapToDto)
                .toList();
        return PaginatedResponseDTO.<ReservationDto>builder()
                .content(response)
                .currentPage(reservations.getNumber())
                .totalPages(reservations.getTotalPages())
                .totalItems(reservations.getTotalElements())
                .isFirstPage(reservations.isFirst())
                .isLastPage(reservations.isLast())
                .build();
    }

    @Override
    public ReservationDto getAReservation(Long eventId, String email) {
        Reservation reservation = reservationRepository.findByEventIdAndUserEmail(eventId, email)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        return mapToDto(reservation);
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

    @Override
    public List<String> getUserEmailsForEvent(Long eventId) {
        List<Reservation> reservations = reservationRepository.findByEventId(eventId);
        return reservations.stream()
                .map(reservation -> reservation.getUser().getEmail())
                .distinct()
                .collect(Collectors.toList());
    }

    private ReservationDto mapToDto(Reservation reservation) {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(reservation.getId());
        reservationDto.setEventId(reservation.getEvent().getId());
        reservationDto.setEventName(reservation.getEvent().getName());
        reservationDto.setEventDate(reservation.getEvent().getDate().toString());
        reservationDto.setAttendeeName(reservation.getUser().getName());
        reservationDto.setUserEmail(reservation.getUser().getEmail());
        reservationDto.setAttendeesCount(reservation.getAttendeesCount());
        return reservationDto;
    }

}

