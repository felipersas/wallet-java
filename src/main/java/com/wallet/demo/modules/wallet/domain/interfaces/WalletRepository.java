package com.wallet.demo.modules.wallet.domain.interfaces;

import java.util.Optional;

import com.wallet.demo.modules.wallet.domain.Wallet;
import com.wallet.demo.modules.wallet.domain.WalletId;
import com.wallet.demo.modules.wallet.domain.OwnerId;

public interface WalletRepository {
  Wallet save(Wallet wallet);

  Optional<Wallet> findById(WalletId id);

  Optional<Wallet> findByOwnerId(OwnerId ownerId);
}
