package com.osayijoy.eventbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Joy Osayi
 * @createdOn Jun-28(Fri)-2024
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
    private Long id;
    private Long eventId;
    private Long userId;
    private int attendeesCount;

}

