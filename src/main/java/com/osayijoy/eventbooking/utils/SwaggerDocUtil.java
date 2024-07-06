package com.osayijoy.eventbooking.utils;
/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */
public class SwaggerDocUtil {

    public static final String AUTHENTICATION_CONTROLLER_SUMMARY = "Authentication Controller";
    public static final String AUTHENTICATION_CONTROLLER_DESCRIPTION = "APIs for user authentication";
    public static final String AUTHENTICATION_CONTROLLER_LOGIN_SUMMARY = "User Login";
    public static final String AUTHENTICATION_CONTROLLER_LOGIN_DESCRIPTION = "Endpoint for user login authentication";

    public static final String RESERVATION_CONTROLLER_SUMMARY = "Reservation Controller";
    public static final String RESERVATION_CONTROLLER_DESCRIPTION = "APIs for reservation controller";

    public static final String USER_CONTROLLER_SUMMARY = "User Controller";
    public static final String USER_CONTROLLER_DESCRIPTION = "APIs for user operations";

    public static final String USER_CONTROLLER_SIGNUP_SUMMARY = "User Signup";
    public static final String USER_CONTROLLER_SIGNUP_DESCRIPTION = "Endpoint to carry out User signup";

    public static final String USER_CONTROLLER_UPDATE_SUMMARY = "User Profile Update";
    public static final String USER_CONTROLLER_UPDATE_DESCRIPTION = "Endpoint to update a user profile";

    public static final String GET_USER_BY_EMAIL_SUMMARY = "Get User";
    public static final String GET_USER_BY_EMAIL_DESCRIPTION = "Retrieve a user by email.";

    public static final String GET_USERS_SUMMARY = "Get All Users";
    public static final String GET_USERS_DESCRIPTION = "Retrieve all users in a paginated format.";

    public static final String DELETE_USER_SUMMARY = "Delete User";
    public static final String DELETE_USER_DESCRIPTION = "Delete a user by email.";


    public static final String EVENT_CONTROLLER_SUMMARY = "Event Management";
    public static final String EVENT_CONTROLLER_DESCRIPTION = "Endpoints for managing events";

    public static final String CREATE_EVENT_SUMMARY = "Create Event";
    public static final String CREATE_EVENT_DESCRIPTION = "Create a new event";

    public static final String UPDATE_EVENT_SUMMARY = "Update Event";
    public static final String UPDATE_EVENT_DESCRIPTION = "Update an existing event";

    public static final String GET_EVENT_BY_ID_SUMMARY = "Get Event by ID";
    public static final String GET_EVENT_BY_ID_DESCRIPTION = "Retrieve an event by its ID";

    public static final String SEARCH_EVENTS_SUMMARY = "Search or Retrieve All Events";
    public static final String SEARCH_EVENTS_DESCRIPTION = "Retrieve events optionally filtered by name, date range, or category";

    public static final String DELETE_EVENT_SUMMARY = "Delete Event";
    public static final String DELETE_EVENT_DESCRIPTION = "Delete an event by its ID";


    public static final String CREATE_RESERVATION_SUMMARY = "Create Reservation";
    public static final String CREATE_RESERVATION_DESCRIPTION = "Create a new reservation for an event";
    public static final String GET_RESERVATIONS_BY_USER_SUMMARY = "Get Reservations by User";
    public static final String GET_RESERVATIONS_BY_USER_DESCRIPTION = "Retrieve reservations made by a specific user";
    public static final String GET_RESERVATIONS_BY_EVENT_SUMMARY = "Get Reservations by Event";
    public static final String GET_RESERVATIONS_BY_EVENT_DESCRIPTION = "Retrieve reservations made for a specific event";
    public static final String CANCEL_RESERVATION_SUMMARY = "Cancel Reservation";
    public static final String CANCEL_RESERVATION_DESCRIPTION = "Cancel an existing reservation";
    public static final String RESERVATION_MANAGEMENT_TAG = "Reservation Management";

    public static final String RESERVATION_CREATED = "Reservation Created";
    public static final String BAD_REQUEST = "Bad Request";
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String RESERVATIONS_FOUND = "Reservations Found";
    public static final String NOT_FOUND = "Not Found";
    public static final String RESERVATION_CANCELLED = "Reservation Cancelled";

    public static final String EVENT_CREATED = "Event Created";
    public static final String EVENT_UPDATED = "Event Updated";
    public static final String EVENT_FOUND = "Event Found";
    public static final String EVENT_DELETED = "Event Deleted";
    public static final String EVENTS_FOUND = "Events Found";

    public static final String USER_CREATED = "User Created";
    public static final String USER_UPDATED = "User Updated";
    public static final String USER_FOUND = "User Found";
    public static final String USER_DELETED = "User Deleted";
    public static final String USERS_FOUND = "Users Found";

    public static final String LOGIN_SUCCESS = "Login Success";
    public static final String BAD_CREDENTIALS = "Bad Credentials";

    public static final String RESPONSE_CODE_200 = "200";
    public static final String RESPONSE_CODE_201 = "201";
    public static final String RESPONSE_CODE_204 = "204";
    public static final String RESPONSE_CODE_400 = "400";
    public static final String RESPONSE_CODE_401 = "401";
    public static final String RESPONSE_CODE_404 = "404";

    private SwaggerDocUtil() {
    }
}

