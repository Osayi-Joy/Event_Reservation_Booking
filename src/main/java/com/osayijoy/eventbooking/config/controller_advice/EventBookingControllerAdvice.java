package com.osayijoy.eventbooking.config.controller_advice;

import com.osayijoy.eventbooking.exception.ApiError;
import com.osayijoy.eventbooking.exception.BadRequestException;
import com.osayijoy.eventbooking.exception.InsufficientTicketsException;
import com.osayijoy.eventbooking.exception.ResourceNotFoundException;
import com.osayijoy.eventbooking.utils.response.ApiResponseJson;
import com.osayijoy.eventbooking.utils.response.ControllerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

/**
 * @author Joy Osayi
 * @createdOn Jun-28(Fri)-2024
 */

@RestControllerAdvice
public class EventBookingControllerAdvice {

    private static final Logger logger =
            Logger.getLogger(EventBookingControllerAdvice.class.getName());

    @ExceptionHandler({
            AuthenticationException.class,
            InvalidBearerTokenException.class,
            AccessDeniedException.class
    })
    public ResponseEntity<Object> handleAuthenticationException(Exception ex) {

        String message = "You are not authorized to make this request";

        if (ex instanceof InvalidBearerTokenException || ex instanceof BadCredentialsException) {
            message = "The access token supplied is invalid or expired";
        }

        ApiError error = new ApiError(message);
        ApiResponseJson<String> responseJson =
                ApiResponseJson.<String>builder()
                        .data(null)
                        .errors(error)
                        .message("This resource is protected")
                        .success(false)
                        .build();

        return new ResponseEntity<>((responseJson), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(ClassNotFoundException.class)
    public ResponseEntity<Object> handleClassNotFoundException(ClassNotFoundException exception) {
        logger.severe("an error occurred: " + exception);

        return ControllerResponse.buildFailureResponse(
                new ApiError("Kindly reach out to support for help"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException exception) {
        logger.severe("an error occurred: " + exception);
        return ControllerResponse.buildFailureResponse(
                new ApiError(exception.getParsedString().concat(" is not in the valid date format")),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundExceptions(ResourceNotFoundException ex) {
        return ControllerResponse.buildFailureResponse(
                new ApiError(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestExceptions(BadRequestException ex) {
        return ControllerResponse.buildFailureResponse(
                new ApiError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InsufficientTicketsException.class)
    public ResponseEntity<Object> handleInsufficientTicketsException(InsufficientTicketsException ex) {
        return ControllerResponse.buildFailureResponse(
                new ApiError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericExceptions(Exception ex) {
        return ControllerResponse.buildFailureResponse(
                new ApiError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

