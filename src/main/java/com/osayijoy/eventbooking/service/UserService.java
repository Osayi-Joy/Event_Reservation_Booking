package com.osayijoy.eventbooking.service;


import com.osayijoy.eventbooking.dto.UserDto;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */


public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto updateUser(String email, UserDto userDto);

    UserDto getUserByEmail(String email);
}
