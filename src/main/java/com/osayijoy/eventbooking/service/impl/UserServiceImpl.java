package com.osayijoy.eventbooking.service.impl;

import com.osayijoy.eventbooking.dto.request.UserRequestDto;
import com.osayijoy.eventbooking.dto.response.UserResponseDto;
import com.osayijoy.eventbooking.enums.Role;
import com.osayijoy.eventbooking.exception.BadRequestException;
import com.osayijoy.eventbooking.exception.ResourceNotFoundException;
import com.osayijoy.eventbooking.model.User;
import com.osayijoy.eventbooking.repository.UserRepository;
import com.osayijoy.eventbooking.service.UserService;
import com.osayijoy.eventbooking.utils.BeanUtilWrapper;
import com.osayijoy.eventbooking.utils.PaginatedResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new BadRequestException(EMAIL_ALREADY_EXISTS);
        }
        User user = mapToEntity(userDto);
        if (isAdminUser(userDto.getEmail())) {
            user.setRole(Role.ROLE_ADMIN);
        } else {
            user.setRole(Role.ROLE_USER);
        }
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user = userRepository.save(user);
        return mapToDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(String email, UserRequestDto userDto) {
        User user = userRepository.findFirstByEmailOrderByCreatedDate(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user = userRepository.save(user);

        return mapToDto(user);
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findFirstByEmailOrderByCreatedDate(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        return mapToDto(user);
    }

    @Override
    public PaginatedResponseDTO<UserResponseDto> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponseDto> userDtos = userPage.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return PaginatedResponseDTO.<UserResponseDto>builder()
                .content(userDtos)
                .currentPage(userPage.getNumber())
                .totalPages(userPage.getTotalPages())
                .totalItems(userPage.getTotalElements())
                .isFirstPage(userPage.isFirst())
                .isLastPage(userPage.isLast())
                .build();
    }

    @Override
    @Transactional
    public void deleteUserByEmail(String email) {
        User user = userRepository.findFirstByEmailOrderByCreatedDate(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));

        userRepository.delete(user);
    }
    private UserResponseDto mapToDto(User user) {
        UserResponseDto userDto = new UserResponseDto();
        BeanUtilWrapper.copyNonNullProperties(user, userDto);
        return userDto;
    }
    private User mapToEntity(UserRequestDto userDto) {
        User user = new User();
        BeanUtilWrapper.copyNonNullProperties(userDto, user);
        return user;
    }

    private boolean isAdminUser(String email) {
        return email.endsWith("@musulasoft.com");
    }

}
