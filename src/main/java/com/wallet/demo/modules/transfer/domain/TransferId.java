package com.wallet.demo.modules.transfer.domain;

import com.wallet.demo.shared.domain.BaseId;
import java.util.UUID;

public class TransferId extends BaseId {
  public TransferId(UUID value) {
    super(value);
  }

  public static TransferId newId() {
    return new TransferId(generate());
  }

  public static TransferId from(String raw) {
    return new TransferId(parse(raw));
  }

}
