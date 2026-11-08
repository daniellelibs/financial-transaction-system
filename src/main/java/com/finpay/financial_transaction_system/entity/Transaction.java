
package com.finpay.financial_transaction_system.entity;

import com.finpay.financial_transaction_system.enums.TransactionStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionStatus status;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Transaction() {
    }

    public Transaction(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
        this.status = TransactionStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }
}
