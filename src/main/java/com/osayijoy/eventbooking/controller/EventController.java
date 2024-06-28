package com.osayijoy.eventbooking.controller;

import com.osayijoy.eventbooking.service.UserService;
import lombok.*;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */
@RestController
@RequiredArgsConstructor
public class EventController {
    private final UserService userService;
}
