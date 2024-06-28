package com.osayijoy.eventbooking.service.impl;


import java.util.*;
import java.util.stream.Collectors;

import com.osayijoy.eventbooking.config.security.JwtHelper;
import com.osayijoy.eventbooking.dto.UserAuthDetials;
import com.osayijoy.eventbooking.dto.request.Credentials;
import com.osayijoy.eventbooking.dto.response.LoginResponse;
import com.osayijoy.eventbooking.exception.BadRequestException;
import com.osayijoy.eventbooking.exception.ResourceNotFoundException;
import com.osayijoy.eventbooking.model.User;
import com.osayijoy.eventbooking.repository.UserRepository;
import com.osayijoy.eventbooking.service.AuthenticationService;
import com.osayijoy.eventbooking.exception.ErrorConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements UserDetailsService, AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userFoundInDB =
                userRepository
                        .findFirstByEmailOrderByCreatedDate(username)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(ErrorConstants.LOGIN_FAILED));

        return getUserProfileDTO(username, userFoundInDB);
    }

    @Override
    public LoginResponse authenticate(Credentials loginRequestDTO) {
        UserAuthDetials userDetails =
                (UserAuthDetials) this.loadUserByUsername(loginRequestDTO.getEmail());
        if (passwordEncoder.matches(loginRequestDTO.getPassword(), userDetails.getPassword())) {
            return getLoginResponse(loginRequestDTO, userDetails);
        }

    throw new BadRequestException(ErrorConstants.LOGIN_FAILED);
    }



    public LoginResponse getLoginResponse(
            Credentials loginRequestDTO, UserAuthDetials userDetails) {
        Map<String, String> claims =
                getClaims(loginRequestDTO.getEmail(), userDetails);
        Map<String, Object> additionalInformation = new HashMap<>();
        additionalInformation.put("Name", userDetails.getName());
        additionalInformation.put("email", userDetails.getEmail());
        additionalInformation.put("permissions", userDetails.getAuthorities());
       return LoginResponse.builder()
                .accessToken(jwtHelper.createJwtForClaims(loginRequestDTO.getEmail(), claims))
                .additionalInformation(additionalInformation)
                .build();
    }



    private UserAuthDetials getUserProfileDTO(
            String username, User userFoundInDB) {
        UserAuthDetials userProfileDTO = new UserAuthDetials();
        userProfileDTO.setName(userFoundInDB.getName());
        userProfileDTO.setEmail(userFoundInDB.getEmail());
        userProfileDTO.setPassword(userFoundInDB.getPassword());
//        userProfileDTO.setPermissions(getGrantedAuthorities(userFoundInDB.getAssignedRole()));

        return userProfileDTO;
    }

    public static Map<String, String> getClaims(String username, UserAuthDetials userDetails) {
        return buildClaims(username, userDetails, null);
    }



    private static Map<String, String> buildClaims(
            String username, UserAuthDetials userDetails, String resetKey) {
        Map<String, String> claims = new HashMap<>();
        claims.put("username", username);

        String authorities =
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(" "));
        claims.put("permissions", authorities);
        claims.put("email", userDetails.getEmail());
        claims.put("name", userDetails.getName());
        return claims;
    }
}

