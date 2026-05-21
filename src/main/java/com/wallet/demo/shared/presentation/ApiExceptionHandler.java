package com.wallet.demo.shared.presentation;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.wallet.demo.shared.domain.exceptions.DomainException;
import com.wallet.demo.shared.domain.exceptions.DomainException.ErrorType;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ApiExceptionHandler {

  private static final Map<ErrorType, HttpStatus> STATUS_MAP = Map.of(
      ErrorType.NOT_FOUND, HttpStatus.NOT_FOUND,
      ErrorType.CONFLICT, HttpStatus.CONFLICT,
      ErrorType.RULE_VIOLATION, HttpStatus.UNPROCESSABLE_ENTITY
  );

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ApiErrorResponse> handleDomain(DomainException exception) {
    HttpStatus status = STATUS_MAP.getOrDefault(exception.errorType(), HttpStatus.BAD_REQUEST);
    return ResponseEntity.status(status).body(new ApiErrorResponse(exception.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
    List<ApiErrorResponse.FieldError> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
        .map(error -> new ApiErrorResponse.FieldError(error.getField(), error.getDefaultMessage()))
        .toList();
    return ResponseEntity.badRequest().body(new ApiErrorResponse("Validation failed", fieldErrors));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException exception) {
    List<ApiErrorResponse.FieldError> fieldErrors = exception.getConstraintViolations().stream()
        .map(v -> new ApiErrorResponse.FieldError(v.getPropertyPath().toString(), v.getMessage()))
        .toList();
    return ResponseEntity.badRequest().body(new ApiErrorResponse("Validation failed", fieldErrors));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiErrorResponse> handleBadRequest(IllegalArgumentException exception) {
    return ResponseEntity.badRequest().body(new ApiErrorResponse(exception.getMessage()));
  }
}
