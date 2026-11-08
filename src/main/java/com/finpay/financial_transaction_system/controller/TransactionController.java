

package com.finpay.financial_transaction_system.controller;

import com.finpay.financial_transaction_system.dto.CreateTransactionRequest;
import com.finpay.financial_transaction_system.dto.TransactionResponse;
import com.finpay.financial_transaction_system.enums.TransactionStatus;
import com.finpay.financial_transaction_system.service.TransactionService;
import com.stripe.exception.StripeException;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {

        return transactionService.createTransaction(request);
    }

    @GetMapping("/{id}")
    public TransactionResponse getTransaction(@PathVariable UUID id) {
        return transactionService.getTransaction(id);
    }

    @GetMapping
    public Page<TransactionResponse> listTransactions(
            @RequestParam(required = false) TransactionStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return transactionService.listTransactions(
                status, page, size, sortBy, direction
        );
    }
    @PostMapping("/{id}/payment")
    public String createPaymentIntent(@PathVariable UUID id)
            throws StripeException {
        return transactionService.createPaymentIntent(id);
    }
}

