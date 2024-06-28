package com.osayijoy.eventbooking.exception;
/**
 * @author Joy Osayi
 * @createdOn Jun-28(Fri)-2024
 */
public class InsufficientTicketsException extends RuntimeException {
  public InsufficientTicketsException(String message) {
    super(message);
  }
}