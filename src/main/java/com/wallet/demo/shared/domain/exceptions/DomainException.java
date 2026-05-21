package com.wallet.demo.shared.domain.exceptions;

public abstract class DomainException extends RuntimeException {

  public enum ErrorType {
    NOT_FOUND,
    CONFLICT,
    RULE_VIOLATION
  }

  private final ErrorType errorType;

  protected DomainException(String message, ErrorType errorType) {
    super(message);
    this.errorType = errorType;
  }

  public ErrorType errorType() {
    return errorType;
  }
}
