package com.camdigikey.emailservice.kafka;

import com.camdigikey.emailservice.SendEmailMessage;
import com.camdigikey.emailservice.GenericReply;

import com.camdigikey.emailservice.SendEmailReply;
import com.camdigikey.emailservice.dto.SendEmailRequestDto;
import com.camdigikey.emailservice.mapper.MapStructMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
public class SendEmailProducer {

  @Value("${kafka.topic.send-email.request}")
  private String topic;

  private KafkaTemplate genericKafkaTemplate;

  private MapStructMapper mapper;
  @Getter
  private CountDownLatch latch;



  @Autowired
  public SendEmailProducer(KafkaTemplate genericKafkaTemplate, MapStructMapper mapper) {
    this.genericKafkaTemplate = genericKafkaTemplate;
    this.mapper = mapper;
    this.latch = new CountDownLatch(1);
  }

  public void sendEmail(SendEmailRequestDto requestDto) {
    log.info("producing send-email request to {}", topic);
    SendEmailMessage sendEmailMsg = mapper.sendEmailReqToSendEmailMsg(requestDto);

    genericKafkaTemplate.send(topic, sendEmailMsg);
  }

  @KafkaListener(
      topics = "${kafka.topic.send-email.reply}",
      groupId = "groupId",
      containerFactory = "genericListenerFactory"
  )
  public void listenForSendEmailReply(GenericReply reply) {
    log.debug("send-email reply received");
    log.debug(reply.toString());
    SendEmailReply data = reply.getData();
    log.debug(data.toString());

    latch.countDown();
  }

  public void resetLatch() {
    latch = new CountDownLatch(1);
  }
}