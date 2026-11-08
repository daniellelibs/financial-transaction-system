package com.finpay.financial_transaction_system.controller;

import com.finpay.financial_transaction_system.entity.Transaction;
import com.finpay.financial_transaction_system.enums.TransactionStatus;
import com.finpay.financial_transaction_system.repository.TransactionRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/stripe")
public class StripeWebhookController {
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(StripeWebhookController.class);
    private final TransactionRepository transactionRepository;

    @Value("${STRIPE_WEBHOOK_SECRET}")
    private String webhookSecret;

    public StripeWebhookController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @PostMapping("/webhook")
    @ResponseStatus(HttpStatus.OK)
    public void handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature
    ) throws SignatureVerificationException {

        Event event;

        try {
            event = Webhook.constructEvent(
                    payload,
                    signature,
                    webhookSecret
            );
        } catch (Exception e) {
            log.error("Stripe webhook signature verification failed", e);
            throw e;
        }

        if ("payment_intent.succeeded".equals(event.getType())) {

            Optional<PaymentIntent> paymentIntent =
                    event.getDataObjectDeserializer()
                            .getObject()
                            .filter(PaymentIntent.class::isInstance)
                            .map(PaymentIntent.class::cast);

            paymentIntent.ifPresent(intent -> {
                transactionRepository
                        .findByPaymentReference(intent.getId())
                        .ifPresent(transaction -> {
                            transaction.setStatus(TransactionStatus.COMPLETED);
                            transactionRepository.save(transaction);
                        });
            });
        }

        if ("payment_intent.payment_failed".equals(event.getType())) {

            Optional<PaymentIntent> paymentIntent =
                    event.getDataObjectDeserializer()
                            .getObject()
                            .filter(PaymentIntent.class::isInstance)
                            .map(PaymentIntent.class::cast);

            paymentIntent.ifPresent(intent -> {
                transactionRepository
                        .findByPaymentReference(intent.getId())
                        .ifPresent(transaction -> {
                            transaction.setStatus(TransactionStatus.FAILED);
                            transactionRepository.save(transaction);
                        });
            });
        }
    }
}
