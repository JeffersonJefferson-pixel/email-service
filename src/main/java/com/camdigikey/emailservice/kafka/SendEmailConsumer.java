package com.camdigikey.emailservice.kafka;

import com.camdigikey.emailservice.dto.SendEmailResponseDto;
import com.camdigikey.emailservice.exception.EmailException;
import com.camdigikey.emailservice.model.SendEmailRequest;
import com.camdigikey.emailservice.service.SendEmailRequestService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.messaging.handler.annotation.SendTo;

import com.camdigikey.emailservice.email.IEmailService;
import com.camdigikey.emailservice.dto.SendEmailRequestDto;
import com.camdigikey.emailservice.mapper.MapStructMapper;
import com.camdigikey.emailservice.GenericReply;
import com.camdigikey.emailservice.SendEmailMessage;
import com.camdigikey.emailservice.SendEmailReply;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
public class SendEmailConsumer {

  private MapStructMapper mapper;

  private SendEmailRequestService sendEmailReqSvc;

  @Getter
  private CountDownLatch latch;

  @Autowired
  public SendEmailConsumer(MapStructMapper mapper, SendEmailRequestService sendEmailReqSvc) {
    this.mapper = mapper;
    this.sendEmailReqSvc = sendEmailReqSvc;
    this.latch = new CountDownLatch(1);
  }

  @SendTo("${kafka.topic.send-email.reply}")
  @KafkaListener(
      topics = "${kafka.topic.send-email.request}",
      groupId = "groupId",
      containerFactory = "genericListenerFactory"
  )
  GenericReply consumeSendEmailRequest(SendEmailMessage message) {
    log.info("consuming send-email request");
    GenericReply reply;

    SendEmailRequestDto requestDto = mapper.sendEmailMsgToSendEmailReq(message);

    SendEmailReply data = SendEmailReply.newBuilder()
        .setSubject(requestDto.getSubject())
        .setSender(requestDto.getSender())
        .setReceiver(requestDto.getReceiver())
        .build();

    try {
      SendEmailRequest request = sendEmailReqSvc.saveRequest(requestDto);

      sendEmailReqSvc.sendEmailWithRetry(request);

      reply = GenericReply.newBuilder()
          .setCode(0)
          .setData(data)
          .setMessage(SendEmailResponseDto.SUCCESS_MESSAGE)
          .build();
    } catch (EmailException e) {
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      String stackTrace = sw.toString();

      reply = GenericReply.newBuilder()
          .setCode(1)
          .setData(data)
          .setMessage(stackTrace)
          .build();
    }

    latch.countDown();

    return reply;
  }

  public void resetLatch() {
    latch = new CountDownLatch(1);
  }

}
