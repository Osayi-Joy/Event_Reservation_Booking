package com.osayijoy.eventbooking.dto.response;



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

