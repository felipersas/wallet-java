package com.wallet.demo.modules.transfer.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.wallet.demo.modules.transfer.domain.enums.TransferStatus;
import com.wallet.demo.shared.domain.Money;
import com.wallet.demo.shared.domain.WalletId;
import com.wallet.demo.shared.domain.enums.MoneyCurrency;

class TransferTest {

  private final WalletId sourceWalletId = WalletId.newId();
  private final WalletId destinationWalletId = WalletId.newId();
  private final Money amount = Money.create(BigInteger.valueOf(500), MoneyCurrency.BRL);

  @Nested
  class Create {

    @Test
    void shouldCreatePendingTransfer() {
      Transfer transfer = Transfer.create(sourceWalletId, destinationWalletId, amount);

      assertNotNull(transfer.getId());
      assertEquals(sourceWalletId, transfer.getSourceWalletId());
      assertEquals(destinationWalletId, transfer.getDestinationWalletId());
      assertEquals(amount, transfer.getAmount());
      assertEquals(MoneyCurrency.BRL, transfer.getAmount().getCurrency());
      assertEquals(TransferStatus.PENDING, transfer.getStatus());
      assertNotNull(transfer.getCreatedAt());
    }
  }

  @Nested
  class MarkCompleted {

    @Test
    void shouldMarkPendingTransferAsCompleted() {
      Transfer transfer = Transfer.create(sourceWalletId, destinationWalletId, amount);

      transfer.markCompleted();

      assertEquals(TransferStatus.COMPLETED, transfer.getStatus());
    }

    @Test
    void shouldRejectCompletingAlreadyCompletedTransfer() {
      Transfer transfer = Transfer.create(sourceWalletId, destinationWalletId, amount);
      transfer.markCompleted();

      IllegalStateException ex = assertThrows(IllegalStateException.class, transfer::markCompleted);

      assertEquals("Only pending transfers can be marked as completed.", ex.getMessage());
    }

    @Test
    void shouldRejectCompletingCancelledTransfer() {
      Transfer transfer = Transfer.create(sourceWalletId, destinationWalletId, amount);
      transfer.markCancelled();

      assertThrows(IllegalStateException.class, transfer::markCompleted);
    }
  }

  @Nested
  class MarkCancelled {

    @Test
    void shouldMarkPendingTransferAsCancelled() {
      Transfer transfer = Transfer.create(sourceWalletId, destinationWalletId, amount);

      transfer.markCancelled();

      assertEquals(TransferStatus.CANCELLED, transfer.getStatus());
    }

    @Test
    void shouldRejectCancellingCompletedTransfer() {
      Transfer transfer = Transfer.create(sourceWalletId, destinationWalletId, amount);
      transfer.markCompleted();

      IllegalStateException ex = assertThrows(IllegalStateException.class, transfer::markCancelled);

      assertEquals("Only pending transfers can be marked as cancelled.", ex.getMessage());
    }

    @Test
    void shouldRejectCancellingAlreadyCancelledTransfer() {
      Transfer transfer = Transfer.create(sourceWalletId, destinationWalletId, amount);
      transfer.markCancelled();

      assertThrows(IllegalStateException.class, transfer::markCancelled);
    }
  }
}
