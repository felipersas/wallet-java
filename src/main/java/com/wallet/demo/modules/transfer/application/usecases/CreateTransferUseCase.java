package com.wallet.demo.modules.transfer.application.usecases;

import java.math.BigInteger;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import com.wallet.demo.modules.transfer.domain.Transfer;
import com.wallet.demo.modules.transfer.domain.TransferId;
import com.wallet.demo.modules.transfer.domain.interfaces.TransferRepository;
import com.wallet.demo.modules.transfer.domain.interfaces.WalletCreditor;
import com.wallet.demo.modules.transfer.domain.interfaces.WalletDebtor;
import com.wallet.demo.modules.transfer.domain.interfaces.WalletExistenceChecker;
import com.wallet.demo.shared.domain.Money;
import com.wallet.demo.shared.domain.WalletId;
import com.wallet.demo.shared.domain.enums.MoneyCurrency;

@Component
public class CreateTransferUseCase {
  private final TransferRepository transferRepository;
  private final WalletExistenceChecker walletExistenceChecker;
  private final WalletCreditor walletCreditor;
  private final WalletDebtor walletDebtor;
  private final TransactionTemplate txTemplate;

  public CreateTransferUseCase(TransferRepository transferRepository,
      WalletExistenceChecker walletExistenceChecker, WalletCreditor walletCreditor, WalletDebtor walletDebtor,
      TransactionTemplate txTemplate) {
    this.transferRepository = transferRepository;
    this.walletExistenceChecker = walletExistenceChecker;
    this.walletCreditor = walletCreditor;
    this.walletDebtor = walletDebtor;
    this.txTemplate = txTemplate;
  }

  public TransferId execute(WalletId sourceWalletId, WalletId destinationWalletId, BigInteger amount,
      MoneyCurrency currency) {
    walletExistenceChecker.ensureExists(sourceWalletId);
    walletExistenceChecker.ensureExists(destinationWalletId);

    Money money = Money.create(amount, currency);
    TransferId transferId = TransferId.newId();
    processAsync(transferId, sourceWalletId, destinationWalletId, money);
    return transferId;
  }

  @Async
  public void processAsync(TransferId transferId, WalletId sourceWalletId, WalletId destinationWalletId,
      Money money) {
    txTemplate.executeWithoutResult(status -> {
      walletDebtor.debit(sourceWalletId, money);
      walletCreditor.credit(destinationWalletId, money);
      transferRepository.save(Transfer.create(transferId, sourceWalletId, destinationWalletId, money));
    });
  }
}
