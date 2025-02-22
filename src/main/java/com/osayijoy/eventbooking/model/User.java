package com.osayijoy.eventbooking.model;

import com.osayijoy.eventbooking.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User extends Auditable<String> implements Serializable {

    private String name;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
