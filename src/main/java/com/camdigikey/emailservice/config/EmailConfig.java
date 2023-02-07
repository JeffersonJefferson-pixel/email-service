package com.camdigikey.emailservice.config;

import com.camdigikey.emailservice.email.IEmailService;
import com.camdigikey.emailservice.email.MailgunApiService;
import com.camdigikey.emailservice.email.MailgunSmtpService;

import com.camdigikey.emailservice.email.SendgridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {

  @Value("${mail.provider}")
  private String provider;

  @Autowired
  private ApplicationContext appContext;

  @Bean
  public IEmailService emailSvc() {
    switch(provider) {
      case "mailgunSmtp":
        return appContext.getBean(MailgunSmtpService.class);
      case "sendgrid":
        return appContext.getBean(SendgridService.class);
      default:
        return appContext.getBean(MailgunApiService.class);
    }
  }
}
