package com.wallet.demo.modules.transfer.domain.interfaces;

import com.wallet.demo.shared.domain.Money;
import com.wallet.demo.shared.domain.WalletId;

public interface WalletDebtor {
  boolean debit(WalletId walletId, Money amount);
}
