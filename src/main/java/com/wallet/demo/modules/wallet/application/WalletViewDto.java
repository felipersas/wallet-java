package com.wallet.demo.modules.wallet.application;

import com.wallet.demo.modules.wallet.domain.Wallet;
import java.math.BigInteger;

public record WalletViewDto(String id, String ownerId, BigInteger balance) {

  public WalletViewDto(Wallet wallet) {
    this(wallet.id().toString(), wallet.ownerId().toString(), wallet.balance());
  }
}
