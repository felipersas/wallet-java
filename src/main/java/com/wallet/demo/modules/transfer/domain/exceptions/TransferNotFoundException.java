package com.wallet.demo.modules.transfer.domain.exceptions;

import com.wallet.demo.shared.domain.exceptions.DomainException;

public class TransferNotFoundException extends DomainException {
  public TransferNotFoundException(String transferId) {
    super("Transfer not found: " + transferId, ErrorType.NOT_FOUND);
  }
}
