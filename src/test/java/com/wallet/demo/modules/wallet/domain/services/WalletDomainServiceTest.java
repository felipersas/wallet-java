package com.wallet.demo.modules.wallet.domain.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigInteger;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wallet.demo.modules.wallet.domain.OwnerId;
import com.wallet.demo.modules.wallet.domain.Wallet;
import com.wallet.demo.modules.wallet.domain.exceptions.DuplicateOwnerWalletException;
import com.wallet.demo.modules.wallet.domain.interfaces.WalletRepository;

@ExtendWith(MockitoExtension.class)
class WalletDomainServiceTest {

  @Mock
  private WalletRepository walletRepository;

  private WalletDomainService walletDomainService;

  @BeforeEach
  void setUp() {
    walletDomainService = new WalletDomainService(walletRepository);
  }

  @Nested
  class CreateWallet {

    @Test
    void shouldCreateWalletWhenNoExistingWalletForOwner() {
      OwnerId ownerId = OwnerId.newId();
      BigInteger balance = BigInteger.valueOf(500);
      when(walletRepository.findByOwnerId(ownerId)).thenReturn(Optional.empty());

      Wallet wallet = walletDomainService.createWallet(ownerId, balance);

      assertNotNull(wallet);
      assertEquals(ownerId, wallet.ownerId());
      assertEquals(balance, wallet.balance());
      verify(walletRepository).findByOwnerId(ownerId);
    }

    @Test
    void shouldCreateWalletWithZeroBalance() {
      OwnerId ownerId = OwnerId.newId();
      when(walletRepository.findByOwnerId(ownerId)).thenReturn(Optional.empty());

      Wallet wallet = walletDomainService.createWallet(ownerId, BigInteger.ZERO);

      assertEquals(BigInteger.ZERO, wallet.balance());
    }

    @Test
    void shouldRejectDuplicateOwner() {
      OwnerId ownerId = OwnerId.newId();
      Wallet existing = Wallet.open(
          com.wallet.demo.modules.wallet.domain.WalletId.newId(),
          ownerId, BigInteger.ZERO);
      when(walletRepository.findByOwnerId(ownerId)).thenReturn(Optional.of(existing));

      DuplicateOwnerWalletException ex = assertThrows(DuplicateOwnerWalletException.class,
          () -> walletDomainService.createWallet(ownerId, BigInteger.ZERO));

      assertTrue(ex.getMessage().contains(ownerId.toString()));
      assertEquals(com.wallet.demo.shared.domain.exceptions.DomainException.ErrorType.CONFLICT, ex.errorType());
    }
  }
}
