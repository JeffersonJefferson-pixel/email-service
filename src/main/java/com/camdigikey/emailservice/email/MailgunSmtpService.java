package com.camdigikey.emailservice.email;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.camdigikey.emailservice.model.SendEmailRequest;
import com.camdigikey.emailservice.exception.EmailException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MailgunSmtpService extends AbstractEmailService {

  private MailgunConfig mailgunConfig;

  private JavaMailSender mailSender;

  @Override
  public void sendEmail(final SendEmailRequest request) throws  EmailException {
    log.debug("Sending email via email with Mailgun");

    try {
      final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
      final MimeMessageHelper email;
      email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

      email.setTo(request.getReceiver());
      email.setSubject(request.getSubject());
      email.setFrom(new InternetAddress(mailgunConfig.getMailFrom(), request.getSender()));

      email.setText(request.getMessage(), true);

      mailSender.send(mimeMessage);
    } catch (MailSendException | MailAuthenticationException | MessagingException | UnsupportedEncodingException e) {
      log.error("send email failed: {}", e);
      throw new EmailException(e);
    }
  }

}