package com.osayijoy.eventbooking.dto;

import lombok.*;

/**
 * @author Joy Osayi
 * @createdOn Jun-28(Fri)-2024
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
    private Long id;
    private Long eventId;
    private String eventName;
    private String eventDate;
    private String attendeeName;
    private String userEmail;
    private int attendeesCount;

}

