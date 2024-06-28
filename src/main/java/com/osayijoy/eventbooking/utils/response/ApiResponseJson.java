package com.osayijoy.eventbooking.utils.response;


import com.osayijoy.eventbooking.exception.ApiError;
import lombok.*;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ApiResponseJson<T> {
    private String message;
    private boolean success;
    private T data;
    private ApiError errors;


}
