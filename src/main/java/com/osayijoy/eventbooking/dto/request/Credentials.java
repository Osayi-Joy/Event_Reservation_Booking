package com.osayijoy.eventbooking.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Credentials {
    private String email;
    private String password;
}
