package com.wrap.it.service.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.wrap.it.dto.payment.PaymentDto;
import com.wrap.it.exception.EntityNotFoundException;
import com.wrap.it.exception.NotExpiredPaymentException;
import com.wrap.it.exception.StripeSessionException;
import com.wrap.it.mapper.PaymentMapper;
import com.wrap.it.model.Order;
import com.wrap.it.model.Payment;
import com.wrap.it.repository.OrderRepository;
import com.wrap.it.repository.PaymentRepository;
import com.wrap.it.service.PaymentService;
import com.wrap.it.service.StripeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderRepository orderRepository;
    private final StripeService stripeService;

    @Override
    public Page<PaymentDto> findAll(Long userId, Pageable pageable) {
        return paymentRepository.findAll(pageable, userId)
                .map(paymentMapper::toDto);
    }

    @Override
    @Transactional
    public PaymentDto createPayment(Long orderId,
                                    UriComponentsBuilder uriComponentsBuilder) {
        Order order = orderRepository.findFinishedById(orderId).orElseThrow(() ->
                new EntityNotFoundException("Order with id "
                        + orderId + " don't exist or not finished"));

        Payment payment = paymentMapper.toModel(order);
        payment.setStatus(Payment.Status.PENDING);

        Session session;
        try {
            session = stripeService.createSession(order.getTotal(), uriComponentsBuilder, order);
        } catch (StripeException e) {
            throw new StripeSessionException("Can't create stripe session", e);
        }

        payment.setSessionId(session.getId());
        payment.setSessionUrl(session.getUrl());
        paymentRepository.save(payment);

        return paymentMapper.toDto(payment);
    }

    @Override
    @Transactional
    public void success(Session session) {
        Payment payment = findPayment(session.getId());
        payment.setStatus(Payment.Status.PAID);
        paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public String cancel(Session session) {
        Payment payment = findPayment(session.getId());
        payment.setStatus(Payment.Status.CANCELED);
        paymentRepository.save(payment);
        String url = session.getUrl();
        return String.format(
                "Payment has been canceled. You can retry the payment"
                        + " using the session link (%s) within the next 24 hours.",
                url
        );
    }

    @Override
    @Transactional
    public PaymentDto renewPaymentSession(Long paymentId,
                                          UriComponentsBuilder uriComponentsBuilder) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));

        if (payment.getStatus() != Payment.Status.EXPIRED) {
            throw new NotExpiredPaymentException("Only expired payments can be renewed");
        }

        try {
            Session newSession = stripeService.createSession(
                    payment.getTotal(),
                    uriComponentsBuilder,
                    payment.getOrder());

            payment.setSessionId(newSession.getId());
            payment.setSessionUrl(newSession.getUrl());
            payment.setStatus(Payment.Status.PENDING);

            paymentRepository.save(payment);

            return paymentMapper.toDto(payment);

        } catch (StripeException e) {
            throw new StripeSessionException("Failed to retrieve Stripe session for "
                    + "payment ID " + payment.getId(), e);
        }
    }

    private Payment findPayment(String sessionId) {
        return paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Payment not found for session ID: " + sessionId));
    }
}
