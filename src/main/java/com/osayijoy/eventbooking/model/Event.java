package com.osayijoy.eventbooking.model;

import com.osayijoy.eventbooking.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */
@Entity
@Table(name = "events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event extends Auditable<String> implements Serializable {

    private String name;
    private LocalDateTime date;
    private int availableAttendeesCount;
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;
}
