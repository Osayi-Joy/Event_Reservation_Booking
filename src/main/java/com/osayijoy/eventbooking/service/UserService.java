package com.osayijoy.eventbooking.service;


import com.osayijoy.eventbooking.dto.request.UserRequestDto;
import com.osayijoy.eventbooking.dto.response.UserResponseDto;
import com.osayijoy.eventbooking.utils.PaginatedResponseDTO;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */


public interface UserService {

    UserResponseDto createUser(UserRequestDto userDto);

    UserResponseDto updateUser(String email, UserRequestDto userDto);

    UserResponseDto getUserByEmail(String email);

    PaginatedResponseDTO<UserResponseDto> getUsers(int page, int size);
    void deleteUserByEmail(String email);
}
