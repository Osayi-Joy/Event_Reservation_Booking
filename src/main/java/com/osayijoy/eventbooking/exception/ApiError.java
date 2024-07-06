package com.osayijoy.eventbooking.exception;

import lombok.*;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class ApiError {
    public ApiError(String message) {
        this.message = message;
    }

    public static final String ERROR_UNKNOWN = "90";

    private String message;

}
