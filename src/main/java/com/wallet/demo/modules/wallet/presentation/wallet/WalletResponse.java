package com.wallet.demo.modules.wallet.presentation.wallet;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.wallet.demo.modules.wallet.application.WalletViewDto;

public record WalletResponse(

    @JsonProperty("id") String id,

    @JsonProperty("owner_id") String ownerId,

    @JsonProperty("balance") BigInteger balance

) {

  public static WalletResponse from(WalletViewDto view) {
    return new WalletResponse(view.id(), view.ownerId(), view.balance());
  }
}
