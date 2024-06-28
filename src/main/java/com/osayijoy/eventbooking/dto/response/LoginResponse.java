package com.osayijoy.eventbooking.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */
@Getter
@Setter
@Builder
@ToString
public class LoginResponse {
    private String accessToken;
    private Map<String, Object> additionalInformation;
}
