package com.wallet.demo.modules.transfer.application.usecases;

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

import com.wallet.demo.modules.transfer.application.TransferViewDto;
import com.wallet.demo.modules.transfer.domain.Transfer;
import com.wallet.demo.modules.transfer.domain.TransferId;
import com.wallet.demo.modules.transfer.domain.exceptions.TransferNotFoundException;
import com.wallet.demo.modules.transfer.domain.interfaces.TransferRepository;
import com.wallet.demo.shared.domain.Money;
import com.wallet.demo.shared.domain.WalletId;
import com.wallet.demo.shared.domain.enums.MoneyCurrency;

@ExtendWith(MockitoExtension.class)
class GetTransferUseCaseTest {

  @Mock
  private TransferRepository transferRepository;

  private GetTransferUseCase getTransferUseCase;

  @BeforeEach
  void setUp() {
    getTransferUseCase = new GetTransferUseCase(transferRepository);
  }

  @Nested
  class Execute {

    @Test
    void shouldReturnDtoWhenTransferExists() {
      TransferId transferId = TransferId.newId();
      WalletId source = WalletId.newId();
      WalletId destination = WalletId.newId();
      Money money = Money.create(BigInteger.valueOf(500), MoneyCurrency.BRL);
      Transfer transfer = Transfer.create(source, destination, money);
      when(transferRepository.findById(transferId)).thenReturn(Optional.of(transfer));

      TransferViewDto dto = getTransferUseCase.execute(transferId);

      assertEquals(transfer.getId().toString(), dto.id());
      assertEquals(source.toString(), dto.fromWalletId());
      assertEquals(destination.toString(), dto.toWalletId());
      assertEquals(money.getAmount(), dto.amount());
      assertEquals(MoneyCurrency.BRL, dto.currency());
    }

    @Test
    void shouldThrowWhenTransferNotFound() {
      TransferId transferId = TransferId.newId();
      when(transferRepository.findById(transferId)).thenReturn(Optional.empty());

      TransferNotFoundException ex = assertThrows(TransferNotFoundException.class,
          () -> getTransferUseCase.execute(transferId));

      assertTrue(ex.getMessage().contains(transferId.toString()));
    }
  }
}
