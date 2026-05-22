package com.wallet.demo.modules.transfer.presentation;

import java.math.BigInteger;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TransferResponse(
        @JsonProperty("id") String id,
        @JsonProperty("source_wallet_id") String sourceWalletId,
        @JsonProperty("destination_wallet_id") String destinationWalletId,
        @JsonProperty("amount") BigInteger amount,
        @JsonProperty("currency") String currency,
        @JsonProperty("status") String status,
        @JsonProperty("created_at") LocalDateTime createdAt) {

}
