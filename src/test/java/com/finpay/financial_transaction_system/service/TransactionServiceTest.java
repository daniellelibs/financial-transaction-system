package com.finpay.financial_transaction_system.service;

import com.finpay.financial_transaction_system.dto.CreateTransactionRequest;
import com.finpay.financial_transaction_system.dto.TransactionResponse;
import com.finpay.financial_transaction_system.entity.Transaction;
import com.finpay.financial_transaction_system.enums.TransactionStatus;
import com.finpay.financial_transaction_system.exception.TransactionNotFoundException;
import com.finpay.financial_transaction_system.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void createTransaction_shouldSaveAndReturnTransaction() {
        CreateTransactionRequest request =
                new CreateTransactionRequest(new BigDecimal("149.90"), "usd");

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TransactionResponse response = transactionService.createTransaction(request);

        assertEquals(new BigDecimal("149.90"), response.amount());
        assertEquals("USD", response.currency());
        assertEquals(TransactionStatus.PENDING, response.status());

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void getTransaction_shouldReturnTransactionWhenFound() {
        UUID id = UUID.randomUUID();
        Transaction transaction = new Transaction(new BigDecimal("200.00"), "USD");

        when(transactionRepository.findById(id))
                .thenReturn(Optional.of(transaction));

        TransactionResponse response = transactionService.getTransaction(id);

        assertEquals(new BigDecimal("200.00"), response.amount());
        assertEquals("USD", response.currency());
        assertEquals(TransactionStatus.PENDING, response.status());

        verify(transactionRepository).findById(id);
    }

    @Test
    void getTransaction_shouldThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();

        when(transactionRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                TransactionNotFoundException.class,
                () -> transactionService.getTransaction(id)
        );

        verify(transactionRepository).findById(id);
    }

    @Test
    void listTransactions_shouldReturnAllTransactions() {
        Transaction transaction = new Transaction(
                new BigDecimal("150.00"), "USD"
        );

        when(transactionRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(transaction)));

        Page<TransactionResponse> response =
                transactionService.listTransactions(
                        null, 0, 10, "createdAt", "desc"
                );

        assertEquals(1, response.getTotalElements());
        assertEquals("USD", response.getContent().get(0).currency());

        verify(transactionRepository).findAll(any(Pageable.class));
    }

    @Test
    void listTransactions_shouldFilterByStatus() {
        Transaction transaction = new Transaction(
                new BigDecimal("200.00"), "USD"
        );

        when(transactionRepository.findByStatus(
                eq(TransactionStatus.PENDING), any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of(transaction)));

        Page<TransactionResponse> response =
                transactionService.listTransactions(
                        TransactionStatus.PENDING, 0, 10,
                        "createdAt", "desc"
                );

        assertEquals(1, response.getTotalElements());
        assertEquals(
                TransactionStatus.PENDING,
                response.getContent().get(0).status()
        );

        verify(transactionRepository).findByStatus(
                eq(TransactionStatus.PENDING), any(Pageable.class)
        );
    }

}