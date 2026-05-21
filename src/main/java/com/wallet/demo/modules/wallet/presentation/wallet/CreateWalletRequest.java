package com.wallet.demo.modules.wallet.presentation.wallet;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateWalletRequest(
    @NotBlank(message = "Owner ID is required") @JsonProperty("owner_id") String ownerId,

    @NotNull @PositiveOrZero @JsonProperty("initial_balance") BigInteger initialBalance) {
}
