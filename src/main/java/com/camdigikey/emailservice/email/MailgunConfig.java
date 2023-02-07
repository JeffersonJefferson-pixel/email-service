package com.camdigikey.emailservice.email;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class MailgunConfig {
  @Value("${spring.mail.properties.mail.smtp.from}")
  private String mailFrom;

  @Value("${spring.mail.api-key}")
  private String apiKey;

  @Value("${spring.mail.api-base-url}")
  private String apiBaseUrl;
}