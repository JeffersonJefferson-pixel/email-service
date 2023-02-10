package com.camdigikey.emailservice.repository;

import com.camdigikey.emailservice.model.SendEmailRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendEmailRequestRepository extends JpaRepository<SendEmailRequest, Integer> {
}
