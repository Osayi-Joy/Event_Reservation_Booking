package com.osayijoy.eventbooking.controller;

import com.osayijoy.eventbooking.dto.request.UserRequestDto;
import com.osayijoy.eventbooking.dto.response.UserResponseDto;
import com.osayijoy.eventbooking.service.UserService;
import com.osayijoy.eventbooking.utils.PaginatedResponseDTO;
import com.osayijoy.eventbooking.utils.response.ControllerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.osayijoy.eventbooking.utils.SwaggerDocUtil.*;
import static com.osayijoy.eventbooking.utils.constants.Constants.*;


/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */

@RestController
@RequestMapping(USER_API_VI)
@Tag(name = USER_CONTROLLER_SUMMARY, description = USER_CONTROLLER_DESCRIPTION)
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  @Operation(
          summary = USER_CONTROLLER_SIGNUP_SUMMARY,
          description = USER_CONTROLLER_SIGNUP_DESCRIPTION,
          tags = USER_CONTROLLER_SUMMARY,
          responses = {
                  @ApiResponse(
                          description = USER_CREATED,
                          responseCode = RESPONSE_CODE_201,
                          content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = UserResponseDto.class))),
                  @ApiResponse(
                          description = BAD_REQUEST,
                          responseCode = RESPONSE_CODE_400,
                          content = @Content(mediaType = APPLICATION_JSON)),
                  @ApiResponse(
                          description = UNAUTHORIZED,
                          responseCode = RESPONSE_CODE_401,
                          content = @Content(mediaType = APPLICATION_JSON))
          }
  )
  public ResponseEntity<Object> createUser(@Valid @RequestBody UserRequestDto userDto) {
    UserResponseDto createdUser = userService.createUser(userDto);
    return ControllerResponse.buildSuccessResponse(createdUser, HttpStatus.CREATED);
  }

  @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
  @PutMapping("/{email}")
  @Operation(
          summary = USER_CONTROLLER_UPDATE_SUMMARY,
          description = USER_CONTROLLER_UPDATE_DESCRIPTION,
          tags = USER_CONTROLLER_SUMMARY,
          responses = {
                  @ApiResponse(
                          description = USER_UPDATED,
                          responseCode = RESPONSE_CODE_200,
                          content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = UserResponseDto.class))),
                  @ApiResponse(
                          description = NOT_FOUND,
                          responseCode = RESPONSE_CODE_404,
                          content = @Content(mediaType = APPLICATION_JSON)),
                  @ApiResponse(
                          description = UNAUTHORIZED,
                          responseCode = RESPONSE_CODE_401,
                          content = @Content(mediaType = APPLICATION_JSON))
          }
  )
  public ResponseEntity<Object> updateUser(@PathVariable String email, @Valid @RequestBody UserRequestDto userDto) {
    UserResponseDto updatedUser = userService.updateUser(email, userDto);
    return ControllerResponse.buildSuccessResponse(updatedUser);
  }

  @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
  @GetMapping("/{email}")
  @Operation(
          summary = GET_USER_BY_EMAIL_SUMMARY,
          description = GET_USER_BY_EMAIL_DESCRIPTION,
          tags = USER_CONTROLLER_SUMMARY,
          responses = {
                  @ApiResponse(
                          description = USER_FOUND,
                          responseCode = RESPONSE_CODE_200,
                          content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = UserResponseDto.class))),
                  @ApiResponse(
                          description = NOT_FOUND,
                          responseCode = RESPONSE_CODE_404,
                          content = @Content(mediaType = APPLICATION_JSON)),
                  @ApiResponse(
                          description = UNAUTHORIZED,
                          responseCode = RESPONSE_CODE_401,
                          content = @Content(mediaType = APPLICATION_JSON))
          }
  )
  public ResponseEntity<Object> getUserByEmail(@PathVariable String email) {
    UserResponseDto user = userService.getUserByEmail(email);
    return ControllerResponse.buildSuccessResponse(user);
  }

  @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
  @GetMapping
  @Operation(
          summary = GET_USERS_SUMMARY,
          description = GET_USERS_DESCRIPTION,
          tags = USER_CONTROLLER_SUMMARY,
          responses = {
                  @ApiResponse(
                          description = USERS_FOUND,
                          responseCode = RESPONSE_CODE_200,
                          content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = PaginatedResponseDTO.class))),
                  @ApiResponse(
                          description = NOT_FOUND,
                          responseCode = RESPONSE_CODE_404,
                          content = @Content(mediaType = APPLICATION_JSON)),
                  @ApiResponse(
                          description = UNAUTHORIZED,
                          responseCode = RESPONSE_CODE_401,
                          content = @Content(mediaType = APPLICATION_JSON))
          }
  )
  public ResponseEntity<Object> getUsers(
          @RequestParam(defaultValue = PAGE_NUMBER_DEFAULT_VALUE, required = false) int page,
          @RequestParam(defaultValue = PAGE_SIZE_DEFAULT_VALUE, required = false) int size) {
    PaginatedResponseDTO<UserResponseDto> users = userService.getUsers(page, size);
    return ControllerResponse.buildSuccessResponse(users);
  }

  @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
  @DeleteMapping("/{email}")
  @Operation(
          summary = DELETE_USER_SUMMARY,
          description = DELETE_USER_DESCRIPTION,
          tags = USER_CONTROLLER_SUMMARY,
          responses = {
                  @ApiResponse(
                          description = USER_DELETED,
                          responseCode = RESPONSE_CODE_204,
                          content = @Content(mediaType = APPLICATION_JSON)),
                  @ApiResponse(
                          description = NOT_FOUND,
                          responseCode = RESPONSE_CODE_404,
                          content = @Content(mediaType = APPLICATION_JSON)),
                  @ApiResponse(
                          description = UNAUTHORIZED,
                          responseCode = RESPONSE_CODE_401,
                          content = @Content(mediaType = APPLICATION_JSON))
          }
  )
  public ResponseEntity<Void> deleteUser(@PathVariable String email) {
    userService.deleteUserByEmail(email);
    return ResponseEntity.noContent().build();
  }
}
