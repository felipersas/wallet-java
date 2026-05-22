package com.wallet.demo.modules.wallet.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.wallet.demo.modules.wallet.domain.OwnerId;
import com.wallet.demo.modules.wallet.domain.Wallet;
import com.wallet.demo.shared.domain.WalletId;

class InMemoryWalletRepositoryTest {

  private InMemoryWalletRepository repository;

  @BeforeEach
  void setUp() {
    repository = new InMemoryWalletRepository();
  }

  @Nested
  class Save {

    @Test
    void shouldPersistAndReturnWallet() {
      Wallet wallet = Wallet.open(WalletId.newId(), OwnerId.newId(), BigInteger.valueOf(100));

      Wallet saved = repository.save(wallet);

      assertSame(wallet, saved);
    }

    @Test
    void shouldOverwriteExistingWallet() {
      WalletId walletId = WalletId.newId();
      Wallet original = Wallet.open(walletId, OwnerId.newId(), BigInteger.valueOf(100));
      Wallet updated = Wallet.open(walletId, OwnerId.newId(), BigInteger.valueOf(200));

      repository.save(original);
      repository.save(updated);

      Optional<Wallet> found = repository.findById(walletId);
      assertTrue(found.isPresent());
      assertEquals(BigInteger.valueOf(200), found.get().balance());
    }
  }

  @Nested
  class FindById {

    @Test
    void shouldReturnWalletWhenExists() {
      WalletId walletId = WalletId.newId();
      Wallet wallet = Wallet.open(walletId, OwnerId.newId(), BigInteger.ZERO);
      repository.save(wallet);

      Optional<Wallet> found = repository.findById(walletId);

      assertTrue(found.isPresent());
      assertEquals(walletId, found.get().id());
    }

    @Test
    void shouldReturnEmptyWhenNotExists() {
      Optional<Wallet> found = repository.findById(WalletId.newId());

      assertTrue(found.isEmpty());
    }
  }

  @Nested
  class FindByOwnerId {

    @Test
    void shouldReturnWalletForOwner() {
      OwnerId ownerId = OwnerId.newId();
      repository.save(Wallet.open(WalletId.newId(), ownerId, BigInteger.valueOf(500)));

      Optional<Wallet> found = repository.findByOwnerId(ownerId);

      assertTrue(found.isPresent());
      assertEquals(ownerId, found.get().ownerId());
    }

    @Test
    void shouldReturnEmptyWhenNoWalletForOwner() {
      Optional<Wallet> found = repository.findByOwnerId(OwnerId.newId());

      assertTrue(found.isEmpty());
    }
  }
}
