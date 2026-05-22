package com.wallet.demo.modules.wallet.infrastructure;

import com.wallet.demo.modules.transfer.domain.interfaces.WalletDebtor;
import com.wallet.demo.modules.wallet.domain.Wallet;
import com.wallet.demo.modules.wallet.domain.exceptions.WalletNotFoundException;
import com.wallet.demo.modules.wallet.domain.interfaces.WalletRepository;
import com.wallet.demo.shared.domain.Money;
import com.wallet.demo.shared.domain.WalletId;

import org.springframework.stereotype.Component;

@Component
public class WalletDebtorImpl implements WalletDebtor {
  private final WalletRepository walletRepository;

  public WalletDebtorImpl(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  @Override
  public boolean debit(WalletId walletId, Money amount) {
    Wallet wallet = walletRepository.findById(walletId)
        .orElseThrow(() -> new WalletNotFoundException(walletId.toString()));
    wallet.debit(amount.getAmount());
    walletRepository.save(wallet);
    return true;
  }
}
