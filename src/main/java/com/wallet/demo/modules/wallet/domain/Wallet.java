package com.wallet.demo.modules.wallet.domain;

import java.math.BigInteger;

import com.wallet.demo.shared.domain.WalletId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "wallets")
public final class Wallet {
  @Id
  @Column(name = "id", nullable = false)
  private final WalletId id;

  @Column(name = "owner_id", nullable = false, unique = true)
  private final OwnerId ownerId;

  @Column(name = "balance", nullable = false)
  private BigInteger balance;

  protected Wallet() {
    this.id = null;
    this.ownerId = null;
    this.balance = null;
  }

  private Wallet(WalletId id, OwnerId ownerId, BigInteger balance) {
    this.id = id;
    this.ownerId = ownerId;
    this.balance = balance;
  }

  public static Wallet open(WalletId id, OwnerId ownerId, BigInteger initialBalance) {
    if (id == null) {
      throw new IllegalArgumentException("Wallet id is required");
    }
    if (ownerId == null) {
      throw new IllegalArgumentException("Owner id is required");
    }
    if (initialBalance == null) {
      throw new IllegalArgumentException("Initial balance is required");
    }
    if (initialBalance.signum() < 0) {
      throw new IllegalArgumentException("Initial balance cannot be negative");
    }
    return new Wallet(id, ownerId, initialBalance);
  }

  public WalletId id() {
    return id;
  }

  public OwnerId ownerId() {
    return ownerId;
  }

  public BigInteger balance() {
    return balance;
  }

  public void credit(BigInteger amount) {
    balance = balance.add(validateAmount(amount));
  }

  public void debit(BigInteger amount) {
    BigInteger validatedAmount = validateAmount(amount);
    if (balance.compareTo(validatedAmount) < 0) {
      throw new IllegalArgumentException("Insufficient balance");
    }
    balance = balance.subtract(validatedAmount);
  }

  private static BigInteger validateAmount(BigInteger amount) {
    if (amount == null) {
      throw new IllegalArgumentException("Amount is required");
    }
    if (amount.signum() < 0) {
      throw new IllegalArgumentException("Amount cannot be negative");
    }
    return amount;
  }
}
