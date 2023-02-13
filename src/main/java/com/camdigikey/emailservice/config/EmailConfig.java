package com.camdigikey.emailservice.config;

import com.camdigikey.emailservice.email.IEmailService;
import com.camdigikey.emailservice.email.MailgunApiService;
import com.camdigikey.emailservice.email.MailgunSmtpService;

import com.camdigikey.emailservice.email.SendgridService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
@Slf4j
public class EmailConfig {

  @Value("${mail.provider}")
  private String provider;

  @Autowired
  private ApplicationContext appContext;

  @Bean
  @RefreshScope
  public IEmailService emailSvc() {
    switch(provider) {
      case "mailgun-smtp":
        return appContext.getBean(MailgunSmtpService.class);
      case "mailgun-api":
        return appContext.getBean(MailgunApiService.class);
      case "sendgrid":
        return appContext.getBean(SendgridService.class);
      default:
        return appContext.getBean(MailgunApiService.class);
    }
  }
}
