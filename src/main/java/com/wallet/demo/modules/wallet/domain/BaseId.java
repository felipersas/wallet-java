package com.wallet.demo.modules.wallet.domain;

import java.util.Objects;
import java.util.UUID;

public abstract class BaseId {
  private final UUID value;

  protected BaseId(UUID value) {
    this.value = Objects.requireNonNull(value, "Id is required");
  }

  public UUID value() {
    return value;
  }

  public static UUID generate() {
    return UUID.randomUUID();
  }

  public static UUID parse(String raw) {
    return UUID.fromString(raw);
  }

  @Override
  public String toString() {
    return value.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BaseId that = (BaseId) o;
    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }
}
