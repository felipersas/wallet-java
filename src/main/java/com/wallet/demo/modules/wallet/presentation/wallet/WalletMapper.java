package com.wallet.demo.modules.wallet.presentation.wallet;

import com.wallet.demo.modules.wallet.application.WalletViewDto;

public final class WalletMapper {

  private WalletMapper() {}

  public static WalletResponse toResponse(WalletViewDto dto) {
    return new WalletResponse(dto.id(), dto.ownerId(), dto.balance());
  }
}
