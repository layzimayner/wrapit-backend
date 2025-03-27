package com.wrap.it.service;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.wrap.it.model.Order;
import java.math.BigDecimal;
import org.springframework.web.util.UriComponentsBuilder;

public interface StripeService {
    Session createSession(BigDecimal total,
                          UriComponentsBuilder uriComponentsBuilder,
                          Order order) throws StripeException;
}
