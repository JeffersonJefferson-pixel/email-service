package com.camdigikey.emailservice.email;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SendgridConfig {

    @Value("${spring.sendgrid.properties.mail.from}")
    private String sendGridMailFrom;
}
