package com.camdigikey.emailservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.camdigikey.emailservice.listener.SendEmailRetryListener;

@Configuration
@PropertySource("classpath:application.yml")
@EnableRetry
public class AppConfig {

  @Value("${retry.maxAttempts}")
  private int maxAttempts;

  @Value("${retry.maxDelay}")
  private int maxDelay;

  @Autowired
  private ApplicationContext appContext;

  @Bean
  public RetryTemplate sendEmailRetryTemplate() {
    RetryTemplate retryTemplate = new RetryTemplate();

    FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
    fixedBackOffPolicy.setBackOffPeriod(maxDelay);
    retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

    SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
    retryPolicy.setMaxAttempts(maxAttempts);
    retryTemplate.setRetryPolicy(retryPolicy);

    SendEmailRetryListener retryListener = new SendEmailRetryListener();
    retryTemplate.registerListener(retryListener);

    return retryTemplate;
  }
}
