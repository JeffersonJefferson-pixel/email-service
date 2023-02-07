package com.camdigikey.emailservice.mapper;

import com.camdigikey.emailservice.SendEmailMessage;
import org.mapstruct.Mapper;

import com.camdigikey.emailservice.dto.SendEmailRequestDto;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
  SendEmailRequestDto sendEmailMsgToSendEmailReq(SendEmailMessage message);

}
