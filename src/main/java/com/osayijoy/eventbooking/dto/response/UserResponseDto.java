package com.osayijoy.eventbooking.dto.response;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.osayijoy.eventbooking.enums.Role;
import lombok.*;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private String name;

    private String email;

    private Role role;
}

