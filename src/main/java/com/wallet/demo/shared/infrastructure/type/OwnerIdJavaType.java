package com.wallet.demo.shared.infrastructure.type;

import com.wallet.demo.modules.wallet.domain.OwnerId;

public class OwnerIdJavaType extends BaseIdJavaType<OwnerId> {
    public static final OwnerIdJavaType INSTANCE = new OwnerIdJavaType();

    public OwnerIdJavaType() {
        super(OwnerId.class, OwnerId::from);
    }
}
