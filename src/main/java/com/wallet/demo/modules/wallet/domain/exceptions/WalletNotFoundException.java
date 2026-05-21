package com.wallet.demo.modules.wallet.domain.exceptions;

import com.wallet.demo.shared.domain.exceptions.DomainException;

public class WalletNotFoundException extends DomainException {
  public WalletNotFoundException(String walletId) {
    super("Wallet not found: " + walletId, ErrorType.NOT_FOUND);
  }
}
