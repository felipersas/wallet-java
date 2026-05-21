package com.wallet.demo.modules.wallet.application.usecases;

import com.wallet.demo.modules.wallet.application.WalletViewDto;
import com.wallet.demo.modules.wallet.domain.exceptions.WalletNotFoundException;
import com.wallet.demo.modules.wallet.domain.WalletId;
import com.wallet.demo.modules.wallet.domain.interfaces.WalletRepository;

import org.springframework.stereotype.Component;

@Component
public class GetWalletUseCase {

  private final WalletRepository walletRepository;

  public GetWalletUseCase(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  public WalletViewDto execute(WalletId walletId) {
    return new WalletViewDto(walletRepository.findById(walletId)
        .orElseThrow(() -> new WalletNotFoundException(walletId.toString())));
  }
}
