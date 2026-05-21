package com.wallet.demo.modules.wallet.application.usecases;

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

import com.wallet.demo.modules.wallet.application.WalletViewDto;
import com.wallet.demo.modules.wallet.domain.OwnerId;
import com.wallet.demo.modules.wallet.domain.Wallet;
import com.wallet.demo.modules.wallet.domain.WalletId;
import com.wallet.demo.modules.wallet.domain.exceptions.WalletNotFoundException;
import com.wallet.demo.modules.wallet.domain.interfaces.WalletRepository;

@ExtendWith(MockitoExtension.class)
class GetWalletUseCaseTest {

  @Mock
  private WalletRepository walletRepository;

  private GetWalletUseCase getWalletUseCase;

  @BeforeEach
  void setUp() {
    getWalletUseCase = new GetWalletUseCase(walletRepository);
  }

  @Nested
  class Execute {

    @Test
    void shouldReturnDtoWhenWalletExists() {
      WalletId walletId = WalletId.newId();
      OwnerId ownerId = OwnerId.newId();
      Wallet wallet = Wallet.open(walletId, ownerId, BigInteger.valueOf(500));
      when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

      WalletViewDto dto = getWalletUseCase.execute(walletId);

      assertEquals(walletId.toString(), dto.id());
      assertEquals(ownerId.toString(), dto.ownerId());
      assertEquals(BigInteger.valueOf(500), dto.balance());
    }

    @Test
    void shouldThrowWhenWalletNotFound() {
      WalletId walletId = WalletId.newId();
      when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

      WalletNotFoundException ex = assertThrows(WalletNotFoundException.class,
          () -> getWalletUseCase.execute(walletId));

      assertTrue(ex.getMessage().contains(walletId.toString()));
    }
  }
}
