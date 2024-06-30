package com.osayijoy.eventbooking.config.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class JwtConfiguration {

  @Bean
  public KeyPair keyPair() {
    return KeyPairGeneratorUtility.generateKeyPair();
  }

  @Bean
  public RSAPrivateKey jwtSigningKey(KeyPair keyPair) {
    return (RSAPrivateKey) keyPair.getPrivate();
  }

  @Bean
  public RSAPublicKey jwtValidationKey(KeyPair keyPair) {
    return (RSAPublicKey) keyPair.getPublic();
  }

  @Bean
  public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) {
    return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
  }
}


