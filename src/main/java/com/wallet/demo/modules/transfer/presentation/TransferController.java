package com.wallet.demo.modules.transfer.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wallet.demo.modules.transfer.application.usecases.CreateTransferUseCase;
import com.wallet.demo.modules.transfer.application.usecases.GetTransferUseCase;
import com.wallet.demo.modules.transfer.domain.TransferId;
import com.wallet.demo.shared.domain.WalletId;

@RestController
@RequestMapping("/transfers")
public class TransferController {

  private final GetTransferUseCase getTransferUseCase;
  private final CreateTransferUseCase createTransferUseCase;

  public TransferController(GetTransferUseCase getTransferUseCase, CreateTransferUseCase createTransferUseCase) {
    this.getTransferUseCase = getTransferUseCase;
    this.createTransferUseCase = createTransferUseCase;
  }

  @PostMapping
  public void createTransfer(@Valid @RequestBody CreateTransferRequest request) {
    createTransferUseCase.execute(WalletId.from(request.sourceWalletId()),
        WalletId.from(request.destinationWalletId()),
        request.amount(), request.currency());
  }

  @GetMapping("/{id}")
  public TransferResponse getTransfer(@PathVariable String id) {
    return TransferMapper.toResponse(getTransferUseCase.execute(TransferId.from(id)));
  }

}
