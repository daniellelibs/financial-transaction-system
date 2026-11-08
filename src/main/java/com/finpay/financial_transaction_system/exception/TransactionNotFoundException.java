package com.finpay.financial_transaction_system.exception;

import java.util.UUID;

public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(UUID id) {
        super("Transaction not found: " + id);
    }
}