package com.finpay.financial_transaction_system.dto;

import com.finpay.financial_transaction_system.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        BigDecimal amount,
        String currency,
        TransactionStatus status,
        LocalDateTime createdAt
) {}