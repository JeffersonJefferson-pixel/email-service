package com.camdigikey.emailservice.email;


import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

import com.camdigikey.emailservice.model.SendEmailRequest;
import com.camdigikey.emailservice.exception.EmailException;

@Service
@Slf4j
@AllArgsConstructor
public class MailgunApiService extends AbstractEmailService {

  private MailgunConfig mailgunConfig;

  public void sendEmail(final SendEmailRequest request) throws EmailException {
    log.debug("Sending email with Mailgun API");

    try {
      HttpResponse<String> response = postRequest(request, request.getMessage());
      log.debug(String.valueOf(response.getStatus()));
      log.debug(response.getStatusText());
      log.debug(response.getBody());

      if (!response.isSuccess()) {
        throw new EmailException(response.getBody());
      }
    } catch (IOException | UnirestException e) {
      throw new EmailException(e);
    }
  }

  public HttpResponse<String> postRequest(final SendEmailRequest request, String htmlContent) throws UnirestException, IOException {
    return Unirest
        .post(mailgunConfig.getApiBaseUrl())
        .basicAuth("api", mailgunConfig.getApiKey())
        .queryString("from", mailgunConfig.getMailFrom())
        .queryString("to", request.getReceiver())
        .queryString("subject", request.getSubject())
        .queryString("html", htmlContent)
        .asString();
  }
}