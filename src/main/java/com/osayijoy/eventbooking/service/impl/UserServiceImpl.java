package com.osayijoy.eventbooking.service.impl;

import com.osayijoy.eventbooking.dto.UserDto;
import com.osayijoy.eventbooking.exception.BadRequestException;
import com.osayijoy.eventbooking.exception.ResourceNotFoundException;
import com.osayijoy.eventbooking.model.User;
import com.osayijoy.eventbooking.repository.UserRepository;
import com.osayijoy.eventbooking.service.UserService;
import com.osayijoy.eventbooking.utils.BeanUtilWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.osayijoy.eventbooking.exception.ErrorConstants.EMAIL_ALREADY_EXISTS;
import static com.osayijoy.eventbooking.exception.ErrorConstants.USER_NOT_FOUND;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
      throw new BadRequestException(EMAIL_ALREADY_EXISTS);
        }
        User user = userMapper(userDto);
        user = userRepository.save(user);
        return userMapper(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(String email, UserDto userDto) {
        User user = userRepository.findFirstByEmailOrderByCreatedDate(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user = userRepository.save(user);

        return userMapper(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findFirstByEmailOrderByCreatedDate(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        return userMapper(user);
    }

    private UserDto userMapper(User user) {
        UserDto userDto = new UserDto();
        BeanUtilWrapper.copyNonNullProperties(user, userDto);
        return userDto;
    }
    private User userMapper(UserDto userDto) {
        User user = new User();
        BeanUtilWrapper.copyNonNullProperties(userDto, user);
        return user;
    }

}
