
CREATE TABLE transactions (
                              id UUID PRIMARY KEY,
                              amount NUMERIC(19, 2) NOT NULL,
                              currency VARCHAR(3) NOT NULL,
                              status VARCHAR(20) NOT NULL,
                              payment_reference VARCHAR(255),
                              created_at TIMESTAMP NOT NULL
);
