package com.wallet.demo.modules.wallet.domain;

import java.util.UUID;

public class OwnerId extends BaseId {

  public OwnerId(UUID value) {
    super(value);
  }

  public static OwnerId newId() {
    return new OwnerId(generate());
  }

  public static OwnerId from(String raw) {
    return new OwnerId(parse(raw));
  }

}
