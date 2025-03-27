package com.wrap.it.service.impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.wrap.it.exception.StripeSessionException;
import com.wrap.it.model.Order;
import com.wrap.it.model.Payment;
import com.wrap.it.repository.PaymentRepository;
import com.wrap.it.service.StripeService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class StripeServiceImpl implements StripeService {
    private static final String CURRENCY = "usd";
    private final PaymentRepository paymentRepository;

    public StripeServiceImpl(@Value("${stripe.secret.key}") String secretKey,
                             PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        Stripe.apiKey = secretKey;
    }

    @Override
    public Session createSession(BigDecimal total,
                                 UriComponentsBuilder uriBuilder,
                                 Order order)
            throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .putMetadata("orderId", String.valueOf(order.getId()))
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(uriBuilder
                        .scheme("http")
                        .host("44.203.84.198")
                        .port(8080)
                        .path("/payments/success")
                        .queryParam("session_id", "{CHECKOUT_SESSION_ID}")
                        .build()
                        .toString())
                .setCancelUrl(uriBuilder.path("/payments/cancel").build().toString())
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(CURRENCY)
                                                .setUnitAmount(total
                                                        .multiply(BigDecimal
                                                                .valueOf(100)).longValue())
                                                .setProductData(
                                                        SessionCreateParams
                                                                .LineItem
                                                                .PriceData
                                                                .ProductData
                                                                .builder()
                                                                .setName("Order Payment")
                                                                .build()
                                                )
                                                .build()
                                )
                                .setQuantity(1L)
                                .build()
                )
                .build();

        return Session.create(params);
    }

    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void checkExpiredSessions() {
        List<Payment> pendingPayments = paymentRepository
                .findByStatus(Payment.Status.PENDING);

        for (Payment payment : pendingPayments) {
            try {
                Session session = Session.retrieve(payment.getSessionId());
                if (session.getExpiresAt() * 1000 < System.currentTimeMillis()) {
                    payment.setStatus(Payment.Status.EXPIRED);
                    paymentRepository.save(payment);
                }
            } catch (StripeException e) {
                throw new StripeSessionException("Failed to retrieve Stripe session for payment ID "
                        + payment.getId(), e);
            }
        }
    }
}
