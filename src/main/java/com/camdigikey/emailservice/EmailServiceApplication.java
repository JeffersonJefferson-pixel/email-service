package com.camdigikey.emailservice;

import com.camdigikey.emailservice.kafka.SendEmailProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class EmailServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailServiceApplication.class, args);
	}

	@Bean
	@Profile("!test")
	CommandLineRunner commandLineRunner(SendEmailProducer sendEmailProducer) {
		return args -> {
			sendEmailProducer.sendEmail("alice@example.com", "bob@example.com", "Test", "testing!");
		};
	}

}
