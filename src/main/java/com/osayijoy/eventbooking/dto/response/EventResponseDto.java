package com.osayijoy.eventbooking.dto.response;

import com.osayijoy.eventbooking.enums.Category;
import lombok.*;

import java.time.LocalDate;

/**
 * @author Joy Osayi
 * @createdOn Jun-28(Fri)-2024
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseDto {
    private Long id;
    private String name;
    private LocalDate date;
    private int availableAttendeesCount;
    private String description;
    private Category category;

}
