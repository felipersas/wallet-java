package com.wallet.demo.modules.wallet.application.usecases;

import com.wallet.demo.modules.wallet.application.WalletViewDto;
import com.wallet.demo.modules.wallet.domain.OwnerId;
import com.wallet.demo.modules.wallet.domain.Wallet;
import com.wallet.demo.modules.wallet.domain.interfaces.WalletRepository;
import com.wallet.demo.modules.wallet.domain.services.WalletDomainService;

import java.math.BigInteger;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CreateWalletUseCase {

  private final WalletDomainService walletDomainService;
  private final WalletRepository walletRepository;

  public CreateWalletUseCase(WalletDomainService walletDomainService, WalletRepository walletRepository) {
    this.walletDomainService = walletDomainService;
    this.walletRepository = walletRepository;
  }

  @Async
  public WalletViewDto execute(OwnerId ownerId, BigInteger initialBalance) {
    Wallet wallet = walletDomainService.createWallet(ownerId, initialBalance);
    return new WalletViewDto(walletRepository.save(wallet));
  }
}
