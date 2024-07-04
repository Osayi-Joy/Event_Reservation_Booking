package com.osayijoy.eventbooking.utils.response;


import com.osayijoy.eventbooking.exception.ApiError;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */

public class ControllerResponse {
 private ControllerResponse() {
 }

 private static final String DEFAULT_SUCCESS_MESSAGE = "Request Successfully Treated";
 public static ResponseEntity<Object> buildSuccessResponse(Object responseData, String message ){
  return ResponseEntity.ok(ApiResponseJson.builder()
          .success(true)

          .data(responseData)
          .message(StringUtils.isBlank(message)?DEFAULT_SUCCESS_MESSAGE:message)
          .errors(null)
          .build());

 }

 public static   ResponseEntity<Object> buildSuccessResponse(Object responseData){
  return ResponseEntity.ok(ApiResponseJson.builder()
          .success(true)
          .data(responseData)
          .message(DEFAULT_SUCCESS_MESSAGE)
          .errors(null)
          .build());

 }
 public static   ResponseEntity<Object> buildSuccessResponse(Object responseData, HttpStatus status){
  return ResponseEntity.status(status).body(ApiResponseJson.builder()
          .success(true)
          .data(responseData)
          .message(DEFAULT_SUCCESS_MESSAGE)
          .errors(null)
          .build());

 }

 public static   ResponseEntity<Object> buildSuccessResponse(){
  return ResponseEntity.ok(ApiResponseJson.builder()
          .success(true)

          .data(null)
          .message(DEFAULT_SUCCESS_MESSAGE)
          .errors(null)
          .build());

 }



 public static ResponseEntity<Object> buildFailureResponse(ApiError apiError, HttpStatus httpStatus ){
  String message = "";
  if (httpStatus.is4xxClientError()){
   message = "Kindly ensure you are passing the right information, check the errors field for what could be wrong with your request";
  }else if (httpStatus.is5xxServerError()){
   message = "Oops, sorry we were unable to process your request, reach out to support for help";
  }
  return new ResponseEntity<>(ApiResponseJson.builder()
          .success(false)
          .data(null)
          .message(message)
          .errors(apiError)
          .build(), httpStatus);

 }
}
