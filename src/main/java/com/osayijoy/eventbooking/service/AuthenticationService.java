package com.osayijoy.eventbooking.service;

import com.osayijoy.eventbooking.dto.request.Credentials;
import com.osayijoy.eventbooking.dto.response.LoginResponse;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */
public interface AuthenticationService {
    LoginResponse authenticate(Credentials loginRequestDTO);
}
