package com.wallet.demo.modules.transfer.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.wallet.demo.modules.transfer.domain.Transfer;
import com.wallet.demo.modules.transfer.domain.interfaces.TransferRepository;
import com.wallet.demo.modules.transfer.domain.interfaces.WalletCreditor;
import com.wallet.demo.modules.transfer.domain.interfaces.WalletDebtor;
import com.wallet.demo.modules.transfer.domain.interfaces.WalletExistenceChecker;
import com.wallet.demo.shared.domain.Money;
import com.wallet.demo.shared.domain.WalletId;
import com.wallet.demo.shared.domain.enums.MoneyCurrency;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CreateTransferUseCaseTest {

  @Mock
  private TransferRepository transferRepository;

  @Mock
  private WalletExistenceChecker walletExistenceChecker;

  @Mock
  private WalletCreditor walletCreditor;

  @Mock
  private WalletDebtor walletDebtor;

  @Mock
  private PlatformTransactionManager txManager;

  private CreateTransferUseCase createTransferUseCase;

  @BeforeEach
  void setUp() {
    TransactionTemplate txTemplate = new TransactionTemplate(txManager);
    when(txManager.getTransaction(any())).thenReturn(null);
    doNothing().when(txManager).commit(any());
    createTransferUseCase = new CreateTransferUseCase(transferRepository, walletExistenceChecker,
        walletCreditor, walletDebtor, txTemplate);
  }

  @Nested
  class Execute {

    @Test
    void shouldCreateTransferWhenBothWalletsExist() {
      WalletId source = WalletId.newId();
      WalletId destination = WalletId.newId();
      when(walletDebtor.debit(eq(source), any(Money.class))).thenReturn(true);
      when(walletCreditor.credit(eq(destination), any(Money.class))).thenReturn(true);

      createTransferUseCase.execute(source, destination, BigInteger.valueOf(500), MoneyCurrency.BRL);

      verify(walletExistenceChecker).ensureExists(source);
      verify(walletExistenceChecker).ensureExists(destination);
      verify(walletDebtor).debit(eq(source), any(Money.class));
      verify(walletCreditor).credit(eq(destination), any(Money.class));
      verify(transferRepository).save(any(Transfer.class));
    }

    @Test
    void shouldThrowWhenSourceWalletNotFound() {
      WalletId source = WalletId.newId();
      WalletId destination = WalletId.newId();
      doThrow(new RuntimeException("Wallet not found")).when(walletExistenceChecker).ensureExists(source);

      assertThrows(RuntimeException.class,
          () -> createTransferUseCase.execute(source, destination, BigInteger.valueOf(500), MoneyCurrency.BRL));

      verify(transferRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenDestinationWalletNotFound() {
      WalletId source = WalletId.newId();
      WalletId destination = WalletId.newId();
      doThrow(new RuntimeException("Wallet not found")).when(walletExistenceChecker).ensureExists(destination);

      assertThrows(RuntimeException.class,
          () -> createTransferUseCase.execute(source, destination, BigInteger.valueOf(500), MoneyCurrency.BRL));

      verify(transferRepository, never()).save(any());
    }
  }
}
