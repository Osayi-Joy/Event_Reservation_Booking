package com.osayijoy.eventbooking.service;

import com.osayijoy.eventbooking.dto.response.ReservationDto;
import com.osayijoy.eventbooking.utils.PaginatedResponseDTO;

import java.util.List;

/**
 * @author Joy Osayi
 * @createdOn Jun-28(Fri)-2024
 */
public interface ReservationService {
    ReservationDto createReservation(Long eventId, String email, int attendeesCount);

    PaginatedResponseDTO<ReservationDto> getReservationsByUser(String email, int page, int size);

    PaginatedResponseDTO<ReservationDto> getReservationsByEvent(Long eventId, int page, int size);

    ReservationDto getAReservation(Long eventId, String email);

    void cancelReservation(Long reservationId);

    List<String> getUserEmailsForEvent(Long eventId);
}
