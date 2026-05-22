package com.wallet.demo.modules.transfer.domain.interfaces;

import com.wallet.demo.modules.transfer.domain.Transfer;
import com.wallet.demo.modules.transfer.domain.TransferId;

import java.util.Optional;

public interface TransferRepository {

  Transfer save(Transfer transfer);

  Optional<Transfer> findById(TransferId id);

}
