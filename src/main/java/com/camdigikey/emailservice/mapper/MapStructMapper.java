package com.camdigikey.emailservice.mapper;

import com.camdigikey.emailservice.SendEmailMessage;
import com.camdigikey.emailservice.model.SendEmailRequest;
import org.mapstruct.Mapper;

import com.camdigikey.emailservice.dto.SendEmailRequestDto;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
  SendEmailRequestDto sendEmailMsgToSendEmailReq(SendEmailMessage message);

  SendEmailMessage sendEmailReqToSendEmailMsg(SendEmailRequestDto requestDto);
}
