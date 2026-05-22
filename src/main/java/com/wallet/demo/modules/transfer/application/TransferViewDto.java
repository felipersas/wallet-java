package com.wallet.demo.modules.transfer.application;

import com.wallet.demo.modules.transfer.domain.Transfer;
import com.wallet.demo.modules.transfer.domain.enums.TransferStatus;
import com.wallet.demo.shared.domain.Money;
import com.wallet.demo.shared.domain.enums.MoneyCurrency;

import java.time.LocalDateTime;

public record TransferViewDto(
    String id,
    String fromWalletId,
    String toWalletId,
    Money amount,
    MoneyCurrency currency,
    TransferStatus status,
    LocalDateTime createdAt) {
  public TransferViewDto(Transfer transfer) {
    this(transfer.getId().toString(), transfer.getSourceWalletId().toString(),
        transfer.getDestinationWalletId().toString(),
        transfer.getAmount(), transfer.getCurrency(),
        transfer.getStatus(), transfer.getCreatedAt());
  }
}
