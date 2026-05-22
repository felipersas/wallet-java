package com.wallet.demo.modules.transfer.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.wallet.demo.modules.transfer.domain.Transfer;
import com.wallet.demo.modules.transfer.domain.TransferId;
import com.wallet.demo.shared.domain.Money;
import com.wallet.demo.shared.domain.WalletId;
import com.wallet.demo.shared.domain.enums.MoneyCurrency;

class InMemoryTransferRepositoryTest {

  private InMemoryTransferRepository repository;

  @BeforeEach
  void setUp() {
    repository = new InMemoryTransferRepository();
  }

  @Nested
  class Save {

    @Test
    void shouldPersistAndReturnTransfer() {
      Transfer transfer = Transfer.create(WalletId.newId(), WalletId.newId(),
          Money.create(BigInteger.valueOf(500), MoneyCurrency.BRL));

      Transfer saved = repository.save(transfer);

      assertSame(transfer, saved);
    }

    @Test
    void shouldOverwriteExistingTransfer() {
      TransferId transferId = TransferId.newId();
      Transfer original = Transfer.create(WalletId.newId(), WalletId.newId(),
          Money.create(BigInteger.valueOf(100), MoneyCurrency.BRL));
      Transfer updated = Transfer.create(WalletId.newId(), WalletId.newId(),
          Money.create(BigInteger.valueOf(200), MoneyCurrency.BRL));

      repository.save(original);
      repository.save(updated);

      Optional<Transfer> found = repository.findById(updated.getId());
      assertTrue(found.isPresent());
      assertEquals(BigInteger.valueOf(200), found.get().getAmount().getAmount());
    }
  }

  @Nested
  class FindById {

    @Test
    void shouldReturnTransferWhenExists() {
      Transfer transfer = Transfer.create(WalletId.newId(), WalletId.newId(),
          Money.create(BigInteger.valueOf(500), MoneyCurrency.BRL));
      repository.save(transfer);

      Optional<Transfer> found = repository.findById(transfer.getId());

      assertTrue(found.isPresent());
      assertEquals(transfer.getId(), found.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenNotExists() {
      Optional<Transfer> found = repository.findById(TransferId.newId());

      assertTrue(found.isEmpty());
    }
  }
}
