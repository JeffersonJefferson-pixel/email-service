package com.camdigikey.emailservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequestDto {

  private String subject;
  private String message;
  private String sender = "CamDigiKey";
  private String receiver;
}
