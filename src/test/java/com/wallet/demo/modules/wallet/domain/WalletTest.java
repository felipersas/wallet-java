package com.wallet.demo.modules.wallet.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.wallet.demo.shared.domain.WalletId;

class WalletTest {

  private final WalletId walletId = WalletId.newId();
  private final OwnerId ownerId = OwnerId.newId();

  @Nested
  class Open {

    @Test
    void shouldCreateWalletWithZeroBalance() {
      Wallet wallet = Wallet.open(walletId, ownerId, BigInteger.ZERO);

      assertNotNull(wallet);
      assertEquals(walletId, wallet.id());
      assertEquals(ownerId, wallet.ownerId());
      assertEquals(BigInteger.ZERO, wallet.balance());
    }

    @Test
    void shouldCreateWalletWithPositiveBalance() {
      BigInteger balance = BigInteger.valueOf(1000);

      Wallet wallet = Wallet.open(walletId, ownerId, balance);

      assertEquals(balance, wallet.balance());
    }

    @Test
    void shouldRejectNullWalletId() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Wallet.open(null, ownerId, BigInteger.ZERO));

      assertEquals("Wallet id is required", ex.getMessage());
    }

    @Test
    void shouldRejectNullOwnerId() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Wallet.open(walletId, null, BigInteger.ZERO));

      assertEquals("Owner id is required", ex.getMessage());
    }

    @Test
    void shouldRejectNullBalance() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Wallet.open(walletId, ownerId, null));

      assertEquals("Initial balance is required", ex.getMessage());
    }

    @Test
    void shouldRejectNegativeBalance() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> Wallet.open(walletId, ownerId, BigInteger.valueOf(-1)));

      assertEquals("Initial balance cannot be negative", ex.getMessage());
    }
  }

  @Nested
  class Credit {

    @Test
    void shouldIncreaseBalance() {
      Wallet wallet = Wallet.open(walletId, ownerId, BigInteger.valueOf(500));

      wallet.credit(BigInteger.valueOf(300));

      assertEquals(BigInteger.valueOf(800), wallet.balance());
    }

    @Test
    void shouldCreditZeroAmount() {
      Wallet wallet = Wallet.open(walletId, ownerId, BigInteger.valueOf(500));

      wallet.credit(BigInteger.ZERO);

      assertEquals(BigInteger.valueOf(500), wallet.balance());
    }

    @Test
    void shouldRejectNullAmount() {
      Wallet wallet = Wallet.open(walletId, ownerId, BigInteger.ZERO);

      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> wallet.credit(null));

      assertEquals("Amount is required", ex.getMessage());
    }

    @Test
    void shouldRejectNegativeAmount() {
      Wallet wallet = Wallet.open(walletId, ownerId, BigInteger.ZERO);

      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> wallet.credit(BigInteger.valueOf(-1)));

      assertEquals("Amount cannot be negative", ex.getMessage());
    }
  }

  @Nested
  class Debit {

    @Test
    void shouldDecreaseBalance() {
      Wallet wallet = Wallet.open(walletId, ownerId, BigInteger.valueOf(500));

      wallet.debit(BigInteger.valueOf(200));

      assertEquals(BigInteger.valueOf(300), wallet.balance());
    }

    @Test
    void shouldDebitEntireBalance() {
      Wallet wallet = Wallet.open(walletId, ownerId, BigInteger.valueOf(500));

      wallet.debit(BigInteger.valueOf(500));

      assertEquals(BigInteger.ZERO, wallet.balance());
    }

    @Test
    void shouldRejectNullAmount() {
      Wallet wallet = Wallet.open(walletId, ownerId, BigInteger.valueOf(500));

      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> wallet.debit(null));

      assertEquals("Amount is required", ex.getMessage());
    }

    @Test
    void shouldRejectNegativeAmount() {
      Wallet wallet = Wallet.open(walletId, ownerId, BigInteger.valueOf(500));

      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> wallet.debit(BigInteger.valueOf(-1)));

      assertEquals("Amount cannot be negative", ex.getMessage());
    }

    @Test
    void shouldRejectExcessiveAmount() {
      Wallet wallet = Wallet.open(walletId, ownerId, BigInteger.valueOf(100));

      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> wallet.debit(BigInteger.valueOf(200)));

      assertEquals("Insufficient balance", ex.getMessage());
      assertEquals(BigInteger.valueOf(100), wallet.balance());
    }
  }
}
