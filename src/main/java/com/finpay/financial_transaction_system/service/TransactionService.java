

package com.finpay.financial_transaction_system.service;

import com.finpay.financial_transaction_system.dto.CreateTransactionRequest;
import com.finpay.financial_transaction_system.dto.TransactionResponse;
import com.finpay.financial_transaction_system.entity.Transaction;
import com.finpay.financial_transaction_system.enums.TransactionStatus;
import com.finpay.financial_transaction_system.repository.TransactionRepository;
import com.finpay.financial_transaction_system.exception.TransactionNotFoundException;
import com.finpay.financial_transaction_system.integration.StripePaymentService;
import com.stripe.exception.StripeException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final StripePaymentService stripePaymentService;

    public TransactionService(
            TransactionRepository transactionRepository,
            StripePaymentService stripePaymentService) {
        this.transactionRepository = transactionRepository;
        this.stripePaymentService = stripePaymentService;
    }

    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest request) {
        String currency = request.currency().toUpperCase();

        Transaction transaction = new Transaction(request.amount(), currency);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return toResponse(savedTransaction);
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransaction(UUID id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));

        return toResponse(transaction);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponse> listTransactions(
            TransactionStatus status,
            int page,
            int size,
            String sortBy,
            String direction) {

        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException("Invalid pagination parameters");
        }

        if (!sortBy.equals("createdAt")
                && !sortBy.equals("amount")
                && !sortBy.equals("status")) {
            throw new IllegalArgumentException("Invalid sort field");
        }

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );

        Page<Transaction> transactions;

        if (status != null) {
            transactions = transactionRepository.findByStatus(status, pageable);
        } else {
            transactions = transactionRepository.findAll(pageable);
        }

        return transactions.map(this::toResponse);
    }

    private TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getStatus(),
                transaction.getCreatedAt()
        );
    }
    @Transactional
    public String createPaymentIntent(UUID transactionId)
            throws StripeException {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));

        if (transaction.getStatus() != TransactionStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending transactions can be paid"
            );
        }

        var paymentIntent = stripePaymentService.createPaymentIntent(transaction);

        transaction.setPaymentReference(paymentIntent.getId());

        transactionRepository.save(transaction);

        return paymentIntent.getClientSecret();
    }
}
