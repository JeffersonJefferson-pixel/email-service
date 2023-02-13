package com.camdigikey.emailservice.controller;

import com.camdigikey.emailservice.dto.SendEmailRequestDto;
import com.camdigikey.emailservice.kafka.SendEmailProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/email")
public class EmailController {

  private SendEmailProducer sendEmailProducer;

  @Autowired
  public EmailController(SendEmailProducer sendEmailProducer) {
    this.sendEmailProducer = sendEmailProducer;
  }

  @PostMapping("/send-email")
  public ResponseEntity sendEmail(@RequestBody SendEmailRequestDto requestDto) {

    sendEmailProducer.sendEmail(requestDto);

    return new ResponseEntity<>("Email delivery is in progress!", HttpStatus.OK);
  }
}
