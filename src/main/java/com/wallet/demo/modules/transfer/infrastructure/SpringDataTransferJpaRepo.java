package com.wallet.demo.modules.transfer.infrastructure;

import com.wallet.demo.modules.transfer.domain.Transfer;
import com.wallet.demo.modules.transfer.domain.TransferId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTransferJpaRepo extends JpaRepository<Transfer, TransferId> {
}
