package com.osayijoy.eventbooking.dto.request;

import com.osayijoy.eventbooking.enums.Category;
import lombok.*;

import java.time.LocalDate;

/**
 * @author Joy Osayi
 * @createdOn Jun-28(Fri)-2024
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventRequestDTO {
    private String name;
    private LocalDate date;
    private int availableAttendeesCount;
    private String description;
    private Category category;
}
