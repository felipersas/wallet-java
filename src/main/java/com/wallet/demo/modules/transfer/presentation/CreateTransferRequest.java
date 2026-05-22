package com.wallet.demo.modules.transfer.presentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wallet.demo.shared.domain.enums.MoneyCurrency;

public record CreateTransferRequest(

    @JsonProperty("source_wallet_id") @NotBlank(message = "Source wallet ID is required") String sourceWalletId,

    @JsonProperty("destination_wallet_id") @NotBlank(message = "Destination wallet ID is required") String destinationWalletId,

    @JsonProperty("amount") @NotNull(message = "Amount is required") BigInteger amount,

    @JsonProperty("currency") @NotNull(message = "Currency is required") MoneyCurrency currency) {
}