package com.wallet.demo.modules.transfer.infrastructure;

import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.wallet.demo.modules.transfer.domain.Transfer;
import com.wallet.demo.modules.transfer.domain.TransferId;
import com.wallet.demo.modules.transfer.domain.interfaces.TransferRepository;

@Repository
public class InMemoryTransferRepository implements TransferRepository {
  private final Map<TransferId, Transfer> storage = new HashMap<>();

  @Override
  public Transfer save(Transfer transfer) {
    storage.put(transfer.getId(), transfer);
    return transfer;
  }

  @Override
  public Optional<Transfer> findById(TransferId id) {
    return Optional.ofNullable(storage.get(id));
  }
}
