package com.wallet.demo.shared.infrastructure.type;

import com.wallet.demo.modules.transfer.domain.TransferId;

public class TransferIdJavaType extends BaseIdJavaType<TransferId> {
    public static final TransferIdJavaType INSTANCE = new TransferIdJavaType();

    public TransferIdJavaType() {
        super(TransferId.class, TransferId::from);
    }
}
