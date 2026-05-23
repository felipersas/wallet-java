package com.wallet.demo.modules.transfer.domain;

import com.wallet.demo.modules.transfer.domain.enums.TransferStatus;
import com.wallet.demo.shared.domain.Money;
import com.wallet.demo.shared.domain.WalletId;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
public class Transfer {
  @Id
  @Column(name = "id", nullable = false)
  private final TransferId id;

  @Column(name = "source_wallet_id", nullable = false)
  private final WalletId sourceWalletId;

  @Column(name = "dest_wallet_id", nullable = false)
  private final WalletId destinationWalletId;

  @Embedded
  @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false))
  @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false, length = 3))
  private final Money amount;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 30)
  private TransferStatus status;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  protected Transfer() {
    this.id = null;
    this.sourceWalletId = null;
    this.destinationWalletId = null;
    this.amount = null;
    this.createdAt = null;
  }

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

  public static Transfer create(TransferId transferId, WalletId sourceWalletId, WalletId destinationWalletId,
      Money amount) {
    return new Transfer(transferId, sourceWalletId, destinationWalletId, amount, LocalDateTime.now());
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
