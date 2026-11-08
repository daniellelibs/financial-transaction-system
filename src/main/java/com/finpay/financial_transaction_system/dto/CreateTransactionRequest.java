
package com.finpay.financial_transaction_system.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

public record CreateTransactionRequest(

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        @Digits(integer = 17, fraction = 2, message = "Amount must have at most 2 decimal places")
        BigDecimal amount,

        @NotBlank(message = "Currency is required")
        @Pattern(regexp = "^[A-Za-z]{3}$",
                message = "Currency must contain exactly 3 letters")
        String currency

) {}
