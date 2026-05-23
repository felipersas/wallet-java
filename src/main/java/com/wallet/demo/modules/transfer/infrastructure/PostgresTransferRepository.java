package com.wallet.demo.modules.transfer.infrastructure;

import com.wallet.demo.modules.transfer.domain.Transfer;
import com.wallet.demo.modules.transfer.domain.TransferId;
import com.wallet.demo.modules.transfer.domain.interfaces.TransferRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("postgres")
public class PostgresTransferRepository implements TransferRepository {

    private final SpringDataTransferJpaRepo jpaRepo;

    public PostgresTransferRepository(SpringDataTransferJpaRepo jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Transfer save(Transfer transfer) {
        return jpaRepo.save(transfer);
    }

    @Override
    public Optional<Transfer> findById(TransferId id) {
        return jpaRepo.findById(id);
    }
}
