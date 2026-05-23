CREATE TABLE transfers (
    id               UUID PRIMARY KEY,
    source_wallet_id UUID NOT NULL REFERENCES wallets(id),
    dest_wallet_id   UUID NOT NULL REFERENCES wallets(id),
    amount           NUMERIC(38, 0) NOT NULL,
    currency         VARCHAR(3) NOT NULL,
    status           VARCHAR(30) NOT NULL,
    created_at       TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_transfers_source_wallet ON transfers(source_wallet_id);
CREATE INDEX idx_transfers_dest_wallet ON transfers(dest_wallet_id);
CREATE INDEX idx_transfers_status ON transfers(status);
CREATE INDEX idx_transfers_created_at ON transfers(created_at);
