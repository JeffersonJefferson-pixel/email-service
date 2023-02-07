package com.camdigikey.emailservice.email;

import com.camdigikey.emailservice.dto.SendEmailRequestDto;
import com.camdigikey.emailservice.exception.EmailException;

import javax.mail.MessagingException;
import java.io.IOException;

public interface IEmailService {

  void sendEmail(final SendEmailRequestDto request) throws EmailException;
}
