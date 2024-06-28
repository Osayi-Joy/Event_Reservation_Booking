package com.osayijoy.eventbooking.controller;

import com.osayijoy.eventbooking.dto.UserDto;
import com.osayijoy.eventbooking.service.UserService;
import com.osayijoy.eventbooking.utils.SwaggerDocUtil;
import com.osayijoy.eventbooking.utils.response.ControllerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.osayijoy.eventbooking.utils.constants.Constants.USER_API_VI;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */

@RestController
@RequestMapping(USER_API_VI)
@Tag(name = SwaggerDocUtil.USER_CONTROLLER_SUMMARY, description = SwaggerDocUtil.USER_CONTROLLER_DESCRIPTION)
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping
  @Operation(
          summary = SwaggerDocUtil.USER_CONTROLLER_SIGNUP_SUMMARY,
          description = SwaggerDocUtil.USER_CONTROLLER_SIGNUP_DESCRIPTION)
  public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {
    UserDto createdUser = userService.createUser(userDto);
    return ControllerResponse.buildSuccessResponse(createdUser);
  }

  @PutMapping("/{email}")
  @Operation(
          summary = SwaggerDocUtil.USER_CONTROLLER_UPDATE_SUMMARY,
          description = SwaggerDocUtil.USER_CONTROLLER_UPDATE_DESCRIPTION)
  public ResponseEntity<Object> updateUser(@PathVariable String email, @Valid @RequestBody UserDto userDto) {
    UserDto updatedUser = userService.updateUser(email, userDto);
    return ControllerResponse.buildSuccessResponse(updatedUser);
  }

  @GetMapping("/{email}")
  @Operation(
          summary = SwaggerDocUtil.GET_USER_BY_EMAIL_SUMMARY,
          description = SwaggerDocUtil.AUTHENTICATION_CONTROLLER_LOGIN_DESCRIPTION)
  public ResponseEntity<Object> getUserByEmail(@PathVariable String email) {
    UserDto user = userService.getUserByEmail(email);
    return ControllerResponse.buildSuccessResponse(user);
  }
}
