package com.camdigikey.emailservice.service;

import com.camdigikey.emailservice.dto.SendEmailRequestDto;
import com.camdigikey.emailservice.email.IEmailService;
import com.camdigikey.emailservice.exception.EmailException;
import com.camdigikey.emailservice.model.SendEmailRequest;
import com.camdigikey.emailservice.repository.SendEmailRequestRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class SendEmailRequestService {

  private SendEmailRequestRepository sendEmailRequestRepo;

  private IEmailService emailSvc;

  private RetryTemplate sendEmailRetryTemplate;

  @Autowired
  public SendEmailRequestService(SendEmailRequestRepository sendEmailRequestRepo, IEmailService emailSvc, RetryTemplate sendEmailRetryTemplate ) {
    this.sendEmailRequestRepo = sendEmailRequestRepo;
    this.emailSvc = emailSvc;
    this.sendEmailRetryTemplate = sendEmailRetryTemplate;
  }

  public SendEmailRequest sendEmailWithRetry(SendEmailRequest request) throws EmailException {

    sendEmailRetryTemplate.execute(arg0 -> {

      try {
        emailSvc.sendEmail(request);
      } catch (EmailException e) {
        throw e;
      } finally {
        incrementNumTries(request);
      }
      return null;
    });

    return request;
  }

  public SendEmailRequest saveRequest(SendEmailRequestDto requestDto) {
    LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);;
    SendEmailRequest request = SendEmailRequest.builder()
        .sender(requestDto.getSender())
        .receiver(requestDto.getReceiver())
        .subject(requestDto.getSubject())
        .message(requestDto.getMessage())
        .requestedAt(now)
        .numTries(0)
        .build();

    return sendEmailRequestRepo.save(request);
  }
  public void incrementNumTries(SendEmailRequest request) {
    request.setNumTries(request.getNumTries() + 1);
    sendEmailRequestRepo.save(request);
  }
}
