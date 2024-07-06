package com.osayijoy.eventbooking.controller;

import com.osayijoy.eventbooking.dto.request.Credentials;
import com.osayijoy.eventbooking.dto.response.LoginResponse;
import com.osayijoy.eventbooking.service.AuthenticationService;
import com.osayijoy.eventbooking.utils.SwaggerDocUtil;
import com.osayijoy.eventbooking.utils.response.ControllerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static com.osayijoy.eventbooking.utils.SwaggerDocUtil.*;
import static com.osayijoy.eventbooking.utils.constants.Constants.APPLICATION_JSON;
import static com.osayijoy.eventbooking.utils.constants.Constants.AUTHENTICATION_API_VI;

/**
 * @author Joy Osayi
 * @createdOn Jun-27(Thu)-2024
 */

@RestController
@RequestMapping(AUTHENTICATION_API_VI)
@Tag(name = SwaggerDocUtil.AUTHENTICATION_CONTROLLER_SUMMARY, description = SwaggerDocUtil.AUTHENTICATION_CONTROLLER_DESCRIPTION)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(
            summary = SwaggerDocUtil.AUTHENTICATION_CONTROLLER_LOGIN_SUMMARY,
            description = SwaggerDocUtil.AUTHENTICATION_CONTROLLER_LOGIN_DESCRIPTION,
            tags = SwaggerDocUtil.AUTHENTICATION_CONTROLLER_SUMMARY,
            responses = {
                    @ApiResponse(
                            description = LOGIN_SUCCESS,
                            responseCode = RESPONSE_CODE_200,
                            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(
                            description = BAD_CREDENTIALS,
                            responseCode = RESPONSE_CODE_401,
                            content = @Content(mediaType = APPLICATION_JSON)),
                    @ApiResponse(
                            description = SwaggerDocUtil.BAD_REQUEST,
                            responseCode = RESPONSE_CODE_400,
                            content = @Content(mediaType = APPLICATION_JSON))
            }
    )
    public ResponseEntity<Object> login(@Valid @RequestBody Credentials loginRequestDTO) {
        LoginResponse response = authenticationService.authenticate(loginRequestDTO);
        return ControllerResponse.buildSuccessResponse(response);
    }
}

