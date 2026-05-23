package com.wallet.demo.modules.transfer.application;

import com.wallet.demo.modules.transfer.domain.Transfer;
import com.wallet.demo.modules.transfer.domain.enums.TransferStatus;
import com.wallet.demo.shared.domain.enums.MoneyCurrency;

import java.math.BigInteger;
import java.time.LocalDateTime;

public record TransferViewDto(
    String id,
    String fromWalletId,
    String toWalletId,
    BigInteger amount,
    MoneyCurrency currency,
    TransferStatus status,
    LocalDateTime createdAt) {
  public TransferViewDto(Transfer transfer) {
    this(transfer.getId().toString(), transfer.getSourceWalletId().toString(),
        transfer.getDestinationWalletId().toString(),
        transfer.getAmount().getAmount(), transfer.getAmount().getCurrency(),
        transfer.getStatus(), transfer.getCreatedAt());
  }
}
