package com.wallet.demo.shared.infrastructure.type;

import com.wallet.demo.shared.domain.WalletId;

public class WalletIdJavaType extends BaseIdJavaType<WalletId> {
    public static final WalletIdJavaType INSTANCE = new WalletIdJavaType();

    public WalletIdJavaType() {
        super(WalletId.class, WalletId::from);
    }
}
