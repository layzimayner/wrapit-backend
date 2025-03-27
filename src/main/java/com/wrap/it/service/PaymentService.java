package com.wrap.it.service;

import com.stripe.model.checkout.Session;
import com.wrap.it.dto.payment.PaymentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

public interface PaymentService {
    Page<PaymentDto> findAll(Long userId, Pageable pageable);

    PaymentDto createPayment(Long orderId, UriComponentsBuilder uriBuilder);

    void success(Session session);

    String cancel(Session session);

    PaymentDto renewPaymentSession(Long paymentId, UriComponentsBuilder uriComponentsBuilder);
}
