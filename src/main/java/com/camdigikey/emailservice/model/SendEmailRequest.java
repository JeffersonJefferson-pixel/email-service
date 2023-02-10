package com.camdigikey.emailservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequest {

  @Id
  @SequenceGenerator(
      name = "send_email_request_id_sequence",
      sequenceName = "send_email_request_id_sequence"
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "sed_email_request_id_sequence"
  )
  private Integer id;
  private String sender;
  private String receiver;
  private LocalDateTime requestedAt;
  private String subject;
  private String message;
  private int numTries;
}
