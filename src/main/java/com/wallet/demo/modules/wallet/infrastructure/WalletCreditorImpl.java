package com.wallet.demo.modules.wallet.infrastructure;

import com.wallet.demo.modules.transfer.domain.interfaces.WalletCreditor;
import com.wallet.demo.modules.wallet.domain.Wallet;
import com.wallet.demo.modules.wallet.domain.exceptions.WalletNotFoundException;
import com.wallet.demo.modules.wallet.domain.interfaces.WalletRepository;
import com.wallet.demo.shared.domain.Money;
import com.wallet.demo.shared.domain.WalletId;

import org.springframework.stereotype.Component;

@Component
public class WalletCreditorImpl implements WalletCreditor {
  private final WalletRepository walletRepository;

  public WalletCreditorImpl(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  @Override
  public boolean credit(WalletId walletId, Money amount) {
    Wallet wallet = walletRepository.findById(walletId)
        .orElseThrow(() -> new WalletNotFoundException(walletId.toString()));
    wallet.credit(amount.getAmount());
    walletRepository.save(wallet);
    return true;
  }
}
