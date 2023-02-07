package com.camdigikey.emailservice.exception;

public class EmailException extends Exception {

  public static final String ERROR_MESSAGE =
      "Email service is unavailable";

  public EmailException() {
    super(ERROR_MESSAGE);
  }

  public EmailException(String message) {
    super(message);
  }

  public EmailException(Throwable cause) {
    super(ERROR_MESSAGE, cause);
  }

  public EmailException(String message, Throwable cause) {
    super(message, cause);
  }
}