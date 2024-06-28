package com.osayijoy.eventbooking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends Auditable<String> implements Serializable {

    private String name;
    private String email;
    private String password;
}
