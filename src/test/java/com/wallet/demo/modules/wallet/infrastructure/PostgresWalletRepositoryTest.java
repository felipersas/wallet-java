package com.wallet.demo.modules.wallet.infrastructure;

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

import com.wallet.demo.modules.wallet.domain.OwnerId;
import com.wallet.demo.modules.wallet.domain.Wallet;
import com.wallet.demo.shared.domain.WalletId;

@SpringBootTest
@ActiveProfiles("postgres")
@Testcontainers
class PostgresWalletRepositoryTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

  @Autowired
  private PostgresWalletRepository repository;

  @Test
  void shouldSaveAndFindById() {
    WalletId id = WalletId.newId();
    OwnerId ownerId = OwnerId.from(java.util.UUID.randomUUID().toString());
    Wallet wallet = Wallet.open(id, ownerId, BigInteger.valueOf(1000));

    repository.save(wallet);

    var found = repository.findById(id);
    assertTrue(found.isPresent());
    assertEquals(id, found.get().id());
    assertEquals(ownerId, found.get().ownerId());
    assertEquals(BigInteger.valueOf(1000), found.get().balance());
  }

  @Test
  void shouldSaveAndFindByOwnerId() {
    WalletId id = WalletId.newId();
    OwnerId ownerId = OwnerId.from(java.util.UUID.randomUUID().toString());
    Wallet wallet = Wallet.open(id, ownerId, BigInteger.valueOf(500));

    repository.save(wallet);

    var found = repository.findByOwnerId(ownerId);
    assertTrue(found.isPresent());
    assertEquals(id, found.get().id());
  }

  @Test
  void shouldReturnEmptyWhenNotFound() {
    var found = repository.findById(WalletId.newId());
    assertTrue(found.isEmpty());
  }
}
