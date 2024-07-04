package com.osayijoy.eventbooking.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.osayijoy.eventbooking.dto.request.UserRequestDto;
import com.osayijoy.eventbooking.dto.response.UserResponseDto;
import com.osayijoy.eventbooking.enums.Role;
import com.osayijoy.eventbooking.exception.BadRequestException;
import com.osayijoy.eventbooking.model.User;
import com.osayijoy.eventbooking.repository.UserRepository;
import com.osayijoy.eventbooking.exception.ResourceNotFoundException;

import com.osayijoy.eventbooking.utils.PaginatedResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Joy Osayi
 * @createdOn Jun-29(Sat)-2024
 */
@Slf4j
class UserServiceImplTest {
  @Mock private UserRepository userRepository;

  @Mock private BCryptPasswordEncoder passwordEncoder;

  @InjectMocks private UserServiceImpl userService;

  private User user;
  private UserRequestDto userDto;
  private User adminUser;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    user = new User();
    user.setId(1L);
    user.setName("Joy Osayi");
    user.setEmail("osayijoy16@gmail.com");
    user.setPassword("password");
    user.setRole(Role.ROLE_USER);

    userDto = new UserRequestDto();
    userDto.setName("Joy Osayi");
    userDto.setEmail("osayijoy16@gmail.com");
    userDto.setPassword("password");

      adminUser = new User();
      adminUser.setId(2L);
      adminUser.setName("Joy Osayi");
      adminUser.setEmail("admin@musulasoft.com");
      adminUser.setPassword("password");
      adminUser.setRole(Role.ROLE_ADMIN);
  }


    @Test
    public void testCreateUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto createdUser = userService.createUser(userDto);

        assertNotNull(createdUser);
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(Role.ROLE_USER, createdUser.getRole());
    }

    @Test
    public void testCreateAdminUser() {

        when(passwordEncoder.encode(anyString())).thenReturn("password");
        when(userRepository.save(any(User.class))).thenReturn(adminUser);

       userDto.setEmail("admin@musulasoft.com");

        UserResponseDto createdAdmin = userService.createUser(userDto);

        assertNotNull(createdAdmin);
        log.info("createdAdmin: {}", createdAdmin);
        assertNotNull(createdAdmin);
        assertEquals(adminUser.getName(), createdAdmin.getName());
        assertEquals(adminUser.getEmail(), createdAdmin.getEmail());
        assertEquals(Role.ROLE_ADMIN, createdAdmin.getRole());
    }

    @Test
    public void testCreateUser_EmailAlreadyExists() {

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.createUser(userDto));
    }

    @Test
    public void testUpdateUser() {

        when(userRepository.findFirstByEmailOrderByCreatedDate(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto updatedUser = userService.updateUser("osayijoy16@gmail.com", userDto);

        assertNotNull(updatedUser);
        assertEquals(user.getName(), updatedUser.getName());
        assertEquals(user.getEmail(), updatedUser.getEmail());
    }

    @Test
    public void testFindUserByEmail() {
        when(userRepository.findFirstByEmailOrderByCreatedDate(anyString()))
                .thenReturn(Optional.of(user));

        UserResponseDto foundUser = userService.getUserByEmail("john.doe@example.com");

        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    public void testFindUserByEmail_NotFound() {
        when(userRepository.findFirstByEmailOrderByCreatedDate(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserByEmail("nonexistent@example.com"));
    }
    @Test
    public void testGetUsers() {

        when(userRepository.findAll(any(Pageable.class))).thenReturn(createPage(user));


        PaginatedResponseDTO<UserResponseDto> users = userService.getUsers(0, 10);


        assertNotNull(users);
        assertEquals(1, users.getContent().size());
        assertEquals(user.getName(), users.getContent().get(0).getName());
        assertEquals(user.getEmail(), users.getContent().get(0).getEmail());
    }

    @Test
    public void testDeleteUser() {

        when(userRepository.findFirstByEmailOrderByCreatedDate(anyString())).thenReturn(Optional.of(user));

        userService.deleteUserByEmail(userDto.getEmail());

        verify(userRepository, times(1)).delete(user);
    }

    private static Page<User> createPage(User user) {
        List<User> userList = Collections.singletonList(user);
        return new PageImpl<>(userList, PageRequest.of(0, 10), userList.size());
    }

}
