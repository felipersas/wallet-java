package com.wallet.demo.modules.wallet.domain.services;

import java.math.BigInteger;

import com.wallet.demo.modules.wallet.domain.OwnerId;
import com.wallet.demo.modules.wallet.domain.Wallet;
import com.wallet.demo.modules.wallet.domain.exceptions.DuplicateOwnerWalletException;
import com.wallet.demo.modules.wallet.domain.interfaces.WalletRepository;
import com.wallet.demo.shared.domain.WalletId;

import org.springframework.stereotype.Service;

@Service
public class WalletDomainService {

  private final WalletRepository walletRepository;

  public WalletDomainService(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  public Wallet createWallet(OwnerId ownerId, BigInteger initialBalance) {
    walletRepository.findByOwnerId(ownerId).ifPresent(w -> {
      throw new DuplicateOwnerWalletException(ownerId.toString());
    });
    return Wallet.open(WalletId.newId(), ownerId, initialBalance);
  }
}
