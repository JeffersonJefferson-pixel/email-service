package com.camdigikey.emailservice;

import com.camdigikey.emailservice.email.IEmailService;
import com.camdigikey.emailservice.exception.EmailException;
import com.camdigikey.emailservice.kafka.SendEmailConsumer;
import com.camdigikey.emailservice.kafka.SendEmailProducer;
import com.camdigikey.emailservice.model.SendEmailRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ActiveProfiles("test")
@DirtiesContext
@EmbeddedKafka(
    partitions = 1,
    brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092",
        "port=9092"
    }
)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KafkaUnitTest {

  @MockBean
  @Qualifier("emailSvc")
  private IEmailService emailSvc;

  @Autowired
  private SendEmailProducer sendEmailProducer;
  @Autowired
  private SendEmailConsumer sendEmailConsumer;

  @BeforeEach
  void setup() {
    sendEmailConsumer.resetLatch();
    sendEmailProducer.resetLatch();
  }

  @Test
  public void givenEmbeddedKafkaBroker_whenProduceSendEmail_thenConsumeSendEmail() throws InterruptedException, EmailException {
    doNothing().when(emailSvc).sendEmail(any(SendEmailRequest.class));

    sendEmailProducer.sendEmail("alice@example.com", "bob@example.com", "Test", "testing!");

    boolean messageConsumed =
        sendEmailConsumer.getLatch().await(10, TimeUnit.SECONDS);
    boolean replyConsumed =
        sendEmailProducer.getLatch().await(10, TimeUnit.SECONDS);

    assertTrue(messageConsumed);
    assertTrue(replyConsumed);
  }
}
