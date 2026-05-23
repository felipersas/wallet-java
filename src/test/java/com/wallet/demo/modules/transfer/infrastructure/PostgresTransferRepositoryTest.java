package com.wallet.demo.modules.transfer.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.wallet.demo.modules.transfer.domain.Transfer;
import com.wallet.demo.modules.transfer.domain.TransferId;
import com.wallet.demo.modules.wallet.domain.OwnerId;
import com.wallet.demo.modules.wallet.domain.Wallet;
import com.wallet.demo.modules.wallet.infrastructure.PostgresWalletRepository;
import com.wallet.demo.shared.domain.Money;
import com.wallet.demo.shared.domain.WalletId;
import com.wallet.demo.shared.domain.enums.MoneyCurrency;

@SpringBootTest
@ActiveProfiles("postgres")
@Testcontainers
class PostgresTransferRepositoryTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

  @Autowired
  private PostgresTransferRepository transferRepository;

  @Autowired
  private PostgresWalletRepository walletRepository;

  @Test
  void shouldSaveAndFindById() {
    WalletId sourceId = WalletId.newId();
    WalletId destId = WalletId.newId();
    saveWallet(sourceId);
    saveWallet(destId);

    Money money = Money.create(BigInteger.valueOf(200), MoneyCurrency.BRL);
    Transfer transfer = Transfer.create(sourceId, destId, money);

    transferRepository.save(transfer);

    var found = transferRepository.findById(transfer.getId());
    assertTrue(found.isPresent());
    assertEquals(sourceId, found.get().getSourceWalletId());
    assertEquals(destId, found.get().getDestinationWalletId());
    assertEquals(BigInteger.valueOf(200), found.get().getAmount().getAmount());
    assertEquals(MoneyCurrency.BRL, found.get().getAmount().getCurrency());
  }

  @Test
  void shouldReturnEmptyWhenNotFound() {
    var found = transferRepository.findById(TransferId.newId());
    assertTrue(found.isEmpty());
  }

  private void saveWallet(WalletId walletId) {
    OwnerId ownerId = OwnerId.from(java.util.UUID.randomUUID().toString());
    walletRepository.save(Wallet.open(walletId, ownerId, BigInteger.valueOf(10000)));
  }
}
