package com.camdigikey.emailservice.email;

import com.camdigikey.emailservice.model.SendEmailRequest;
import com.camdigikey.emailservice.exception.EmailException;

import com.sendgrid.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class SendgridService extends AbstractEmailService {

  private SendGrid sendGrid;

  private SendgridConfig sendgridConfig;

  public void sendEmail(final SendEmailRequest request) throws EmailException {
    log.debug("Sending email with Sendgrid");

    try {
      Email from = new Email(sendgridConfig.getSendGridMailFrom());
      String subject = request.getSubject();
      Email to = new Email(request.getReceiver());

      Content content = new Content("text/html", request.getMessage());

      Mail mail = new Mail(from, subject, to, content);

      Request req = new Request();
      req.setMethod(Method.POST);
      req.setEndpoint("mail/send");
      req.setBody(mail.build());

      Response response = sendGrid.api(req);
      log.debug(String.valueOf(response.getStatusCode()));
      log.debug(response.getBody());
      log.debug(String.valueOf(response.getHeaders()));
    } catch (IOException ex) {
      log.error("failed to send email: {}", ex);
      throw new EmailException(ex);
    }
  }
}
