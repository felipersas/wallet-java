package com.wallet.demo.modules.wallet.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wallet.demo.modules.wallet.application.WalletViewDto;
import com.wallet.demo.modules.wallet.domain.OwnerId;
import com.wallet.demo.modules.wallet.domain.Wallet;
import com.wallet.demo.modules.wallet.domain.exceptions.DuplicateOwnerWalletException;
import com.wallet.demo.modules.wallet.domain.interfaces.WalletRepository;
import com.wallet.demo.modules.wallet.domain.services.WalletDomainService;
import com.wallet.demo.shared.domain.WalletId;

@ExtendWith(MockitoExtension.class)
class CreateWalletUseCaseTest {

  @Mock
  private WalletDomainService walletDomainService;

  @Mock
  private WalletRepository walletRepository;

  private CreateWalletUseCase createWalletUseCase;

  @BeforeEach
  void setUp() {
    createWalletUseCase = new CreateWalletUseCase(walletDomainService, walletRepository);
  }

  @Nested
  class Execute {

    @Test
    void shouldReturnDtoWithWalletData() {
      OwnerId ownerId = OwnerId.newId();
      BigInteger balance = BigInteger.valueOf(1000);
      Wallet wallet = Wallet.open(WalletId.newId(), ownerId, balance);
      when(walletDomainService.createWallet(ownerId, balance)).thenReturn(wallet);
      when(walletRepository.save(wallet)).thenReturn(wallet);

      WalletViewDto dto = createWalletUseCase.execute(ownerId, balance);

      assertEquals(wallet.id().toString(), dto.id());
      assertEquals(ownerId.toString(), dto.ownerId());
      assertEquals(balance, dto.balance());
      verify(walletDomainService).createWallet(ownerId, balance);
      verify(walletRepository).save(wallet);
    }

    @Test
    void shouldPropagateDuplicateOwnerException() {
      OwnerId ownerId = OwnerId.newId();
      when(walletDomainService.createWallet(eq(ownerId), any()))
          .thenThrow(new DuplicateOwnerWalletException(ownerId.toString()));

      assertThrows(DuplicateOwnerWalletException.class,
          () -> createWalletUseCase.execute(ownerId, BigInteger.ZERO));

      verify(walletRepository, never()).save(any());
    }
  }
}
