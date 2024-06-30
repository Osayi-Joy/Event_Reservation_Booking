package com.osayijoy.eventbooking.service.impl;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @author Joy Osayi
 * @createdOn Jun-30(Sun)-2024
 */
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.osayijoy.eventbooking.config.security.JwtHelper;
import com.osayijoy.eventbooking.dto.UserAuthDetials;
import com.osayijoy.eventbooking.dto.request.Credentials;
import com.osayijoy.eventbooking.dto.response.LoginResponse;
import com.osayijoy.eventbooking.enums.Role;
import com.osayijoy.eventbooking.exception.BadRequestException;
import com.osayijoy.eventbooking.exception.ResourceNotFoundException;
import com.osayijoy.eventbooking.model.User;
import com.osayijoy.eventbooking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtHelper jwtHelper;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private User user;
    private UserAuthDetials userAuthDetials;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encryptedPassword");
        user.setRole(Role.ROLE_USER);

        userAuthDetials = new UserAuthDetials();
        userAuthDetials.setName(user.getName());
        userAuthDetials.setEmail(user.getEmail());
        userAuthDetials.setPassword(user.getPassword());
        userAuthDetials.setRole(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(userRepository.findFirstByEmailOrderByCreatedDate(eq("test@example.com"))).thenReturn(Optional.of(user));

        UserDetails userDetails = authenticationService.loadUserByUsername("test@example.com");

        assertThat(userDetails.getUsername()).isEqualTo("test@example.com");
        verify(userRepository, times(1)).findFirstByEmailOrderByCreatedDate(eq("test@example.com"));
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(userRepository.findFirstByEmailOrderByCreatedDate(eq("test@example.com"))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authenticationService.loadUserByUsername("test@example.com"));
        verify(userRepository, times(1)).findFirstByEmailOrderByCreatedDate(eq("test@example.com"));
    }

    @Test
    void testAuthenticate_Success() {
        Credentials credentials = new Credentials();
        credentials.setEmail("test@example.com");
        credentials.setPassword("password");

        when(userRepository.findFirstByEmailOrderByCreatedDate(eq("test@example.com"))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(eq("password"), eq(user.getPassword()))).thenReturn(true);
        when(jwtHelper.createJwtForClaims(any(String.class), any(Map.class))).thenReturn("jwtToken");

        LoginResponse response = authenticationService.authenticate(credentials);

        assertThat(response.getAccessToken()).isEqualTo("jwtToken");
        verify(userRepository, times(1)).findFirstByEmailOrderByCreatedDate(eq("test@example.com"));
        verify(passwordEncoder, times(1)).matches(eq("password"), eq(user.getPassword()));
        verify(jwtHelper, times(1)).createJwtForClaims(any(String.class), any(Map.class));
    }

    @Test
    void testAuthenticate_Failure() {
        Credentials credentials = new Credentials();
        credentials.setEmail("test@example.com");
        credentials.setPassword("wrongPassword");

        when(userRepository.findFirstByEmailOrderByCreatedDate(eq("test@example.com"))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(eq("wrongPassword"), eq(user.getPassword()))).thenReturn(false);

        assertThrows(BadRequestException.class, () -> authenticationService.authenticate(credentials));
        verify(userRepository, times(1)).findFirstByEmailOrderByCreatedDate(eq("test@example.com"));
        verify(passwordEncoder, times(1)).matches(eq("wrongPassword"), eq(user.getPassword()));
    }

    @Test
    void testGetLoginResponse() {
        Credentials credentials = new Credentials();
        credentials.setEmail("test@example.com");

        Map<String, String> claims = new HashMap<>();
        claims.put("username", "test@example.com");

        when(jwtHelper.createJwtForClaims(eq("test@example.com"), any(Map.class))).thenReturn("jwtToken");

        LoginResponse response = authenticationService.getLoginResponse(credentials, userAuthDetials);

        assertThat(response.getAccessToken()).isEqualTo("jwtToken");
        assertThat(response.getAdditionalInformation().get("Name")).isEqualTo("Test User");
        assertThat(response.getAdditionalInformation().get("email")).isEqualTo("test@example.com");
        assertThat(response.getAdditionalInformation().get("Role")).isEqualTo("ROLE_USER");
        verify(jwtHelper, times(1)).createJwtForClaims(eq("test@example.com"), any(Map.class));
    }
}

