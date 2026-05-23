package com.wallet.demo.modules.transfer.presentation;

import com.wallet.demo.modules.transfer.application.TransferViewDto;

public class TransferMapper {
  public TransferMapper() {
  }

  public static TransferResponse toResponse(TransferViewDto transfer) {
    return new TransferResponse(
        transfer.id().toString(),
        transfer.fromWalletId().toString(),
        transfer.toWalletId().toString(),
        transfer.amount(),
        transfer.currency().toString(),
        transfer.status().name(),
        transfer.createdAt());
  }

}
