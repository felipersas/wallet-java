package com.wallet.demo.modules.wallet.infrastructure;

import com.wallet.demo.modules.wallet.domain.OwnerId;
import com.wallet.demo.modules.wallet.domain.Wallet;
import com.wallet.demo.modules.wallet.domain.interfaces.WalletRepository;
import com.wallet.demo.shared.domain.WalletId;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("postgres")
public class PostgresWalletRepository implements WalletRepository {

    private final SpringDataWalletJpaRepo jpaRepo;

    public PostgresWalletRepository(SpringDataWalletJpaRepo jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Wallet save(Wallet wallet) {
        return jpaRepo.save(wallet);
    }

    @Override
    public Optional<Wallet> findById(WalletId id) {
        return jpaRepo.findById(id);
    }

    @Override
    public Optional<Wallet> findByOwnerId(OwnerId ownerId) {
        return jpaRepo.findByOwnerId(ownerId);
    }
}
