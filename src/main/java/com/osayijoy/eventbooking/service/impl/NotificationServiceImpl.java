package com.osayijoy.eventbooking.service.impl;

import com.osayijoy.eventbooking.dto.response.EventResponseDto;
import com.osayijoy.eventbooking.service.EmailService;
import com.osayijoy.eventbooking.service.EventService;
import com.osayijoy.eventbooking.service.NotificationService;
import com.osayijoy.eventbooking.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Joy Osayi
 * @createdOn Jun-30(Sun)-2024
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final EventService eventService;
    private final EmailService emailService;
    private final ReservationService reservationService;

    @Override
    @Scheduled(cron = "0 0 12 * * ?") // Example: run daily at noon
    public void sendUpcomingEventNotifications() {

        List<EventResponseDto> upcomingEvents = eventService.getUpcomingEvents();

        for (EventResponseDto event : upcomingEvents) {
            sendNotificationToUsers(event);
        }
    }


    private void sendNotificationToUsers(EventResponseDto event) {
        List<String> userEmails = reservationService.getUserEmailsForEvent(event.getId());
        String subject = "Upcoming Event Notification";
        String message = "Dear user, the event " + event.getName() + " will start soon. Get ready!";

        for (String userEmail : userEmails) {
            emailService.sendEmail(userEmail, subject, message);
            log.info("Notification sent to {}", userEmail);
        }
    }
}

