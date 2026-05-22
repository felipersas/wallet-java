package com.wallet.demo.modules.transfer.domain;

import com.wallet.demo.modules.transfer.domain.enums.TransferStatus;

import com.wallet.demo.shared.domain.Money;
import com.wallet.demo.shared.domain.WalletId;
import com.wallet.demo.shared.domain.enums.MoneyCurrency;

import java.time.LocalDateTime;

public class Transfer {
  private final TransferId id;
  private final WalletId sourceWalletId;
  private final WalletId destinationWalletId;
  private final Money amount;
  private TransferStatus status;
  private LocalDateTime createdAt;

  private Transfer(TransferId id, WalletId sourceWalletId, WalletId destinationWalletId, Money amount,
      LocalDateTime createdAt) {
    this.id = id;
    this.sourceWalletId = sourceWalletId;
    this.destinationWalletId = destinationWalletId;
    this.status = TransferStatus.PENDING;
    this.amount = amount;
    this.createdAt = createdAt;
  }

  public static Transfer create(WalletId sourceWalletId, WalletId destinationWalletId, Money amount,
      LocalDateTime createdAt) {
    return new Transfer(TransferId.newId(), sourceWalletId, destinationWalletId, amount, createdAt);
  }

  public static Transfer create(WalletId sourceWalletId, WalletId destinationWalletId, Money amount) {
    return new Transfer(TransferId.newId(), sourceWalletId, destinationWalletId, amount, LocalDateTime.now());
  }

  public TransferId getId() {
    return id;
  }

  public WalletId getSourceWalletId() {
    return sourceWalletId;
  }

  public WalletId getDestinationWalletId() {
    return destinationWalletId;
  }

  public TransferStatus getStatus() {
    return status;
  }

  public Money getAmount() {
    return amount;
  }

  public MoneyCurrency getCurrency() {
    return amount.getCurrency();
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void markCompleted() {
    if (status != TransferStatus.PENDING) {
      throw new IllegalStateException("Only pending transfers can be marked as completed.");
    }
    this.status = TransferStatus.COMPLETED;
  }

  public void markCancelled() {
    if (status != TransferStatus.PENDING) {
      throw new IllegalStateException("Only pending transfers can be marked as cancelled.");
    }
    this.status = TransferStatus.CANCELLED;
  }
}
