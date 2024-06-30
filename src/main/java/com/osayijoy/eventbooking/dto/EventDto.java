package com.osayijoy.eventbooking.dto;


import java.time.LocalDate;

import com.osayijoy.eventbooking.enums.Category;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {

    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name cannot be longer than 100 characters")
    private String name;

    @NotNull(message = "Date is mandatory")
    @FutureOrPresent(message = "Date must be in the present or future")
    private LocalDate date;

    @Min(value = 1, message = "Available attendees count must be at least 1")
    @Max(value = 1000, message = "Available attendees count cannot exceed 1000")
    private int availableAttendeesCount;

    @NotBlank(message = "Event description is mandatory")
    @Size(max = 500, message = "Event description cannot be longer than 500 characters")
    private String eventDescription;

    @NotNull(message = "Category is mandatory")
    private Category category;


}

