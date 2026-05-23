CREATE TABLE wallets (
    id         UUID PRIMARY KEY,
    owner_id   UUID NOT NULL UNIQUE,
    balance    NUMERIC(38, 0) NOT NULL,
    CONSTRAINT chk_wallet_balance_nonnegative CHECK (balance >= 0)
);
