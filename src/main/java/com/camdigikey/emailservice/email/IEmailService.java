package com.camdigikey.emailservice.email;

import com.camdigikey.emailservice.model.SendEmailRequest;
import com.camdigikey.emailservice.exception.EmailException;

import javax.mail.MessagingException;
import java.io.IOException;

public interface IEmailService {

  void sendEmail(final SendEmailRequest request) throws EmailException;
}
