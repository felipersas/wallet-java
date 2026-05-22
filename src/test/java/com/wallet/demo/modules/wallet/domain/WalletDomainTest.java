package com.wallet.demo.modules.wallet.domain;

import org.junit.jupiter.api.Test;

import com.wallet.demo.shared.domain.WalletId;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

public class WalletDomainTest {

  OwnerId ownerId = OwnerId.newId();
  WalletId walletId = WalletId.newId();

  @Test
  void openWallet_ShouldReturnOpenWallet() {

    BigInteger initialBalance = BigInteger.ZERO;
    Wallet wallet = Wallet.open(walletId, ownerId, initialBalance);

    assertNotNull(wallet);
    assertEquals(walletId, wallet.id());
    assertEquals(ownerId, wallet.ownerId());
  }

  @Test
  void openWallet_ShouldNotCreateTheWalletIfBalanceIsNegative() {

    BigInteger initialBalance = BigInteger.valueOf(-1);

    assertThrows(IllegalArgumentException.class, () -> Wallet.open(walletId, ownerId, initialBalance));
  }

}
