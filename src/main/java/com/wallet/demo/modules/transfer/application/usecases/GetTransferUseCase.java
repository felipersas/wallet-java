package com.wallet.demo.modules.transfer.application.usecases;

import com.wallet.demo.modules.transfer.application.TransferViewDto;
import com.wallet.demo.modules.transfer.domain.TransferId;
import com.wallet.demo.modules.transfer.domain.exceptions.TransferNotFoundException;
import com.wallet.demo.modules.transfer.domain.interfaces.TransferRepository;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class GetTransferUseCase {
  private final TransferRepository transferRepository;

  public GetTransferUseCase(TransferRepository transferRepository) {
    this.transferRepository = transferRepository;
  }

  @Async
  public TransferViewDto execute(TransferId transferId) {
    return new TransferViewDto(
        transferRepository.findById(transferId)
            .orElseThrow(() -> new TransferNotFoundException(transferId.toString())));
  }
}
