package com.camdigikey.emailservice;

import com.camdigikey.emailservice.dto.SendEmailRequestDto;
import com.camdigikey.emailservice.email.IEmailService;
import com.camdigikey.emailservice.exception.EmailException;
import com.camdigikey.emailservice.model.SendEmailRequest;
import com.camdigikey.emailservice.service.SendEmailRequestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ActiveProfiles("test")
@DirtiesContext
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class SendEmailRetryUnitTest {

  @MockBean
  @Qualifier("emailSvc")
  private IEmailService emailSvc;

  @Autowired
  private SendEmailRequestService sendEmailReqSvc;

  @Test
  public void givenEmailFail_thenTryThreeTimes() throws EmailException {
    doThrow(new EmailException()).when(emailSvc).sendEmail(any(SendEmailRequest.class));

    SendEmailRequestDto requestDto = SendEmailRequestDto.builder()
        .sender("alice@example.com")
        .receiver("bob@example.com")
        .message("Test")
        .subject("testing!")
        .build();

    SendEmailRequest request = sendEmailReqSvc.saveRequest(requestDto);

    try {
      request = sendEmailReqSvc.sendEmailWithRetry(request);
      fail();
    } catch (EmailException e) {
      assertEquals(3, request.getNumAttempts());
    }
  }

  @Test
  public void givenEmailSucceeds_thenTryOnlyOnce() throws EmailException {
    doNothing().when(emailSvc).sendEmail(any(SendEmailRequest.class));

    SendEmailRequestDto requestDto = SendEmailRequestDto.builder()
        .sender("alice@example.com")
        .receiver("bob@example.com")
        .message("Test")
        .subject("testing!")
        .build();

    SendEmailRequest request = sendEmailReqSvc.saveRequest(requestDto);
    request = sendEmailReqSvc.sendEmailWithRetry(request);

    assertEquals(1, request.getNumAttempts());
  }
}
