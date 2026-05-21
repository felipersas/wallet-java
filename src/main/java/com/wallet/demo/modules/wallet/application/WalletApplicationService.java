package com.wallet.demo.modules.wallet.application;

import java.math.BigInteger;

import org.springframework.stereotype.Service;

import com.wallet.demo.modules.wallet.domain.OwnerId;
import com.wallet.demo.modules.wallet.domain.WalletId;
import com.wallet.demo.modules.wallet.application.usecases.CreateWalletUseCase;
import com.wallet.demo.modules.wallet.application.usecases.GetWalletUseCase;

@Service
public class WalletApplicationService {
  private final CreateWalletUseCase createWalletUseCase;
  private final GetWalletUseCase getWalletUseCase;

  public WalletApplicationService(CreateWalletUseCase createWalletUseCase, GetWalletUseCase getWalletUseCase) {
    this.createWalletUseCase = createWalletUseCase;
    this.getWalletUseCase = getWalletUseCase;
  }

  public WalletViewDto createWallet(OwnerId ownerId, BigInteger initialBalance) {
    return new WalletViewDto(createWalletUseCase.execute(ownerId, initialBalance));
  }

  public WalletViewDto getWallet(WalletId walletId) {
    return new WalletViewDto(getWalletUseCase.execute(walletId));
  }
}
