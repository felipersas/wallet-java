package com.wallet.demo.shared.domain;

import java.util.UUID;

public class WalletId extends BaseId {

  public WalletId(UUID value) {
    super(value);
  }

  public static WalletId newId() {
    return new WalletId(generate());
  }

  public static WalletId from(String raw) {
    return new WalletId(parse(raw));
  }
}
