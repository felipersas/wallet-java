package com.wallet.demo.modules.wallet.infrastructure;

import com.wallet.demo.modules.transfer.domain.interfaces.WalletExistenceChecker;
import com.wallet.demo.modules.wallet.domain.exceptions.WalletNotFoundException;
import com.wallet.demo.modules.wallet.domain.interfaces.WalletRepository;
import com.wallet.demo.shared.domain.WalletId;

import org.springframework.stereotype.Component;

@Component
public class WalletExistenceCheckerImpl implements WalletExistenceChecker {
  private final WalletRepository walletRepository;

  public WalletExistenceCheckerImpl(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  @Override
  public void ensureExists(WalletId walletId) {
    if (walletRepository.findById(walletId).isEmpty()) {
      throw new WalletNotFoundException(walletId.toString());
    }
  }
}
