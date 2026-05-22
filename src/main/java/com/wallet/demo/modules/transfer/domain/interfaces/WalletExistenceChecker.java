package com.wallet.demo.modules.transfer.domain.interfaces;

import com.wallet.demo.shared.domain.WalletId;

public interface WalletExistenceChecker {
  void ensureExists(WalletId walletId);
}
