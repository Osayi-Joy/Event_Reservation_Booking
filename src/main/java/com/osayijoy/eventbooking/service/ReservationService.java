package com.osayijoy.eventbooking.service;

import com.osayijoy.eventbooking.dto.ReservationDto;

import java.util.List;

/**
 * @author Joy Osayi
 * @createdOn Jun-28(Fri)-2024
 */
public interface ReservationService {
    ReservationDto createReservation(Long eventId, Long userId, int attendeesCount);
    List<ReservationDto> getReservationsByUser(Long userId);
    List<ReservationDto> getReservationsByEvent(Long eventId);
    void cancelReservation(Long reservationId);

    List<String> getUserEmailsForEvent(Long eventId);
}
