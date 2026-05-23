package com.wallet.demo.shared.domain;

import java.math.BigInteger;

import com.wallet.demo.shared.domain.enums.MoneyCurrency;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class Money {
  @Column(name = "amount", nullable = false)
  private final BigInteger amount;

  @Enumerated(EnumType.STRING)
  @Column(name = "currency", nullable = false, length = 3)
  private MoneyCurrency currency = MoneyCurrency.BRL;

  protected Money() {
    this.amount = null;
  }

  public Money(BigInteger amount, MoneyCurrency currency) {
    if (amount == null || amount.compareTo(BigInteger.ZERO) < 0) {
      throw new IllegalArgumentException("Amount must be a non-negative value.");
    }
    if (currency == null) {
      throw new IllegalArgumentException("Currency cannot be null.");
    }

    this.amount = amount;
    this.currency = currency;
  }

  public static Money create(BigInteger amount, MoneyCurrency currency) {
    return new Money(amount, currency);
  }

  public BigInteger getAmount() {
    return amount;
  }

  public boolean isNegative() {
    return amount.compareTo(BigInteger.ZERO) < 0;
  }

  public boolean isZero() {
    return amount.compareTo(BigInteger.ZERO) == 0;
  }

  public MoneyCurrency getCurrency() {
    return currency;
  }

  public Money add(Money other) {
    if (other == null) {
      throw new IllegalArgumentException("Other money cannot be null.");
    }
    return Money.create(this.amount.add(other.amount), this.currency);
  }

  public Money subtract(Money other) {
    if (other == null) {
      throw new IllegalArgumentException("Other money cannot be null.");
    }
    BigInteger result = this.amount.subtract(other.amount);
    if (result.compareTo(BigInteger.ZERO) < 0) {
      throw new IllegalArgumentException("Resulting amount cannot be negative.");
    }
    return Money.create(result, this.currency);
  }
}
