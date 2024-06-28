package com.osayijoy.eventbooking.config.security;


import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import java.security.KeyPair;

import static com.osayijoy.eventbooking.utils.constants.Constants.AUTHENTICATION_API_VI;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */
@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

  public static final String AUTHORITIES_CLAIM_NAME = "permissions";

  private final CORSFilter corsFilter;


  @Qualifier("delegatedAuthenticationEntryPoint")
  private final DelegatedAuthenticationEntryPoint authEntryPoint;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsFilter))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                                AUTHENTICATION_API_VI.concat("**"),
                        "/swagger-ui.html",
                        "/documentation/**",
                        "/documentation/v3/api-docs/swagger-config",
                        "/documentation/v3/api-docs/swagger-config/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/documentation/v3/api-docs",
                        "/download-license",
                        "/actuator/**",
                        "/api/v1/account-service/administrator/process/accept-invitation/**",
                                "/api/v1/account-service/customers/process/create")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .oauth2ResourceServer(
            oauth2 ->
                oauth2
                    .jwt(jwt -> jwt.jwtAuthenticationConverter(authenticationConverter()))
                    .authenticationEntryPoint(authEntryPoint)
                    .accessDeniedHandler(authEntryPoint));
    return http.build();
  }

  protected JwtAuthenticationConverter authenticationConverter() {
    JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
    authoritiesConverter.setAuthorityPrefix("");
    authoritiesConverter.setAuthoritiesClaimName(AUTHORITIES_CLAIM_NAME);
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
    return converter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public JWT jwt() {
    return new JWT();
  }

//  @Bean
//  public KeyPair keyPair() {
//    return KeyPairGeneratorUtility.generateKeyPair();
//  }
}
