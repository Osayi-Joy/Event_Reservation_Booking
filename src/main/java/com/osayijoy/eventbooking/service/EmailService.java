package com.osayijoy.eventbooking.service;


/**
 * @author Joy Osayi
 * @createdOn Jun-30(Sun)-2024
 */
public interface EmailService {

    void sendEmail(String to, String subject, String text);

}
