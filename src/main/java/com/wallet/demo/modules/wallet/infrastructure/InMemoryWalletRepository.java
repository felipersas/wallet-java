package com.wallet.demo.modules.wallet.infrastructure;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.wallet.demo.modules.wallet.domain.Wallet;
import com.wallet.demo.modules.wallet.domain.WalletId;
import com.wallet.demo.modules.wallet.domain.interfaces.WalletRepository;
import com.wallet.demo.modules.wallet.domain.OwnerId;

@Repository
public class InMemoryWalletRepository implements WalletRepository {
  private final Map<WalletId, Wallet> storage = new ConcurrentHashMap<>();

  @Override
  public Wallet save(Wallet wallet) {
    storage.put(wallet.id(), wallet);
    return wallet;
  }

  @Override
  public Optional<Wallet> findById(WalletId id) {
    return Optional.ofNullable(storage.get(id));
  }

  @Override
  public Optional<Wallet> findByOwnerId(OwnerId ownerId) {
    return storage.values().stream()
        .filter(wallet -> wallet.ownerId().equals(ownerId))
        .findFirst();
  }
}
