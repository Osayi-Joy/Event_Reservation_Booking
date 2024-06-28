package com.osayijoy.eventbooking.config.database;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */
@Configuration
@RequiredArgsConstructor
public class EntityAuditorAware implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    String author = "SYSTEM";
    String loggedInUsername = getLoggedInUsername();
    if (!StringUtils.isEmpty(loggedInUsername)) author = loggedInUsername;

    return Optional.of(author);
  }

  public static String getLoggedInUsername() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth == null ? "SYSTEM" : auth.getName();
  }
}
