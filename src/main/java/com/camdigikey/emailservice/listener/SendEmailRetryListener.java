package com.camdigikey.emailservice.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class SendEmailRetryListener extends RetryListenerSupport {

  @Override
  public <T, E extends Throwable> boolean open(RetryContext context,
                                               RetryCallback<T, E> callback) {
    log.info("init sending email process");

    return super.open(context, callback);
  }

  @Override
  public <T, E extends Throwable> void onError(RetryContext context,
                                               RetryCallback<T, E> callback, Throwable throwable) {
    log.info("retrying to send email again");

    super.onError(context, callback, throwable);
  }

  @Override
  public <T, E extends Throwable> void close(RetryContext context,
                                             RetryCallback<T, E> callback, Throwable throwable) {
    log.info("sending email process ended");
    super.close(context, callback, throwable);
  }


}
