package com.osayijoy.eventbooking.service;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Joy Osayi
 * @createdOn Jun-30(Sun)-2024
 */
public interface NotificationService {

    @Scheduled(cron = "0 0 12 * * ?") // Example: run daily at noon
    void sendUpcomingEventNotifications();
}
