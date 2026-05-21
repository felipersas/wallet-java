package com.wallet.demo.modules.wallet.domain.exceptions;

import com.wallet.demo.shared.domain.exceptions.DomainException;

public class DuplicateOwnerWalletException extends DomainException {
  public DuplicateOwnerWalletException(String ownerId) {
    super("Wallet already exists for owner: " + ownerId, ErrorType.CONFLICT);
  }
}
