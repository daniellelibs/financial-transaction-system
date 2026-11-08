
package com.finpay.financial_transaction_system.integration;

import com.finpay.financial_transaction_system.entity.Transaction;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentService {

    private final StripeClient stripeClient;

    public StripePaymentService(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

    public PaymentIntent createPaymentIntent(Transaction transaction)
            throws StripeException {

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(transaction.getAmount()
                                .movePointRight(2)
                                .longValueExact())
                        .setCurrency(transaction.getCurrency().toLowerCase())
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods
                                        .builder()
                                        .setEnabled(true)
                                        .build())
                        .build();

        return stripeClient.v1().paymentIntents().create(params);
    }
}

