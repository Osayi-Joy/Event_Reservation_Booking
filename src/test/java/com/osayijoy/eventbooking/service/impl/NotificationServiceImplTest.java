package com.osayijoy.eventbooking.service.impl;

/**
 * @author Joy Osayi
 * @createdOn Jun-30(Sun)-2024
 */
import com.osayijoy.eventbooking.dto.response.EventResponseDto;
import com.osayijoy.eventbooking.enums.Category;
import com.osayijoy.eventbooking.service.EmailService;
import com.osayijoy.eventbooking.service.EventService;
import com.osayijoy.eventbooking.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NotificationServiceImplTest {

    @Mock
    private EventService eventService;

    @Mock
    private EmailService emailService;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendUpcomingEventNotifications() {
        // Given
        EventResponseDto event1 = new EventResponseDto(1L, "Event 1", LocalDateTime.now(), 100, "Description 1", Category.CONFERENCE);
        EventResponseDto event2 = new EventResponseDto(2L, "Event 2", LocalDateTime.now(), 150, "Description 2", Category.CONCERT);

        when(eventService.getUpcomingEvents()).thenReturn(Arrays.asList(event1, event2));
        when(reservationService.getUserEmailsForEvent(1L)).thenReturn(Arrays.asList("user1@example.com", "user2@example.com"));
        when(reservationService.getUserEmailsForEvent(2L)).thenReturn(Collections.singletonList("user3@example.com"));

        // When
        notificationService.sendUpcomingEventNotifications();

        // Then

    }

    @Test
    void testSendNotificationToUsers_noEvents() {
        // Given
        when(eventService.getUpcomingEvents()).thenReturn(Collections.emptyList());

        // When
        notificationService.sendUpcomingEventNotifications();

        // Then
        verify(emailService, never()).sendEmail(any(String.class), any(String.class), any(String.class));
    }
}

