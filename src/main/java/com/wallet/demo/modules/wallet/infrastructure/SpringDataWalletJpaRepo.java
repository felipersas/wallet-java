package com.wallet.demo.modules.wallet.infrastructure;

import com.wallet.demo.modules.wallet.domain.OwnerId;
import com.wallet.demo.modules.wallet.domain.Wallet;
import com.wallet.demo.shared.domain.WalletId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataWalletJpaRepo extends JpaRepository<Wallet, WalletId> {
    Optional<Wallet> findByOwnerId(OwnerId ownerId);
}
