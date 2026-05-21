package com.wallet.demo.shared.presentation;

import java.util.List;

public record ApiErrorResponse(String message, List<FieldError> errors) {

  public ApiErrorResponse(String message) {
    this(message, List.of());
  }

  public record FieldError(String field, String message) {}
}
