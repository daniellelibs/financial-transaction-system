
package com.finpay.financial_transaction_system.repository;

import com.finpay.financial_transaction_system.entity.Transaction;
import com.finpay.financial_transaction_system.enums.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Page<Transaction> findByStatus(TransactionStatus status, Pageable pageable);
    Optional<Transaction> findByPaymentReference(String paymentReference);
}

