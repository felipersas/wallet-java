@JavaTypeRegistration(javaType = OwnerId.class, descriptorClass = OwnerIdJavaType.class)
@JavaTypeRegistration(javaType = WalletId.class, descriptorClass = WalletIdJavaType.class)
@JavaTypeRegistration(javaType = TransferId.class, descriptorClass = TransferIdJavaType.class)
package com.wallet.demo.shared.infrastructure.type;

import com.wallet.demo.modules.transfer.domain.TransferId;
import com.wallet.demo.modules.wallet.domain.OwnerId;
import com.wallet.demo.shared.domain.WalletId;
import org.hibernate.annotations.JavaTypeRegistration;
