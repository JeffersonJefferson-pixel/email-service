package com.camdigikey.emailservice;

import com.camdigikey.emailservice.dto.SendEmailRequestDto;
import com.camdigikey.emailservice.model.SendEmailRequest;
import com.camdigikey.emailservice.repository.SendEmailRequestRepository;
import com.camdigikey.emailservice.service.SendEmailRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext
public class SendEmailReqSvcUnitTest {

  @Autowired
  private SendEmailRequestService sendEmailReqSvc;

  @Autowired
  private SendEmailRequestRepository sendEmailReqRepo;

  @Test
  public void givenSaveSendEmailRequest_thenPersistInDb() {
    SendEmailRequestDto requestDto = SendEmailRequestDto.builder()
        .sender("alice@example.com")
        .receiver("bob@example.com")
        .message("Test")
        .subject("testing!")
        .build();
    SendEmailRequest request =  sendEmailReqSvc.saveRequest(requestDto);
    SendEmailRequest foundReq = sendEmailReqRepo.findById(request.getId()).orElseThrow();

    assertEquals(request, foundReq);
  }

  @Test
  public void givenIncrementNumAttempt_thenNumAttemptsIncrease() {
    SendEmailRequestDto requestDto = SendEmailRequestDto.builder()
        .sender("alice@example.com")
        .receiver("bob@example.com")
        .message("Test")
        .subject("testing!")
        .build();

    SendEmailRequest request =  sendEmailReqSvc.saveRequest(requestDto);
    SendEmailRequest foundReq0 = sendEmailReqRepo.findById(request.getId()).orElseThrow();
    assertEquals(0, foundReq0.getNumTries());

    sendEmailReqSvc.incrementNumTries(request);
    SendEmailRequest foundReq1 = sendEmailReqRepo.findById(request.getId()).orElseThrow();
    assertEquals(1, foundReq1.getNumTries());

    sendEmailReqSvc.incrementNumTries(request);
    SendEmailRequest foundReq2 = sendEmailReqRepo.findById(request.getId()).orElseThrow();
    assertEquals(2, foundReq2.getNumTries());
  }
}
