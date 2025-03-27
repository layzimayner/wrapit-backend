package com.wrap.it.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.wrap.it.dto.payment.PaymentDto;
import com.wrap.it.model.Role;
import com.wrap.it.model.User;
import com.wrap.it.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@Tag(name = "Payments management", description = "Endpoints for management payments")
@RequiredArgsConstructor
@RestController
@RequestMapping("/payments")
@Validated
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    @Operation(summary = "Get all payments", description = "Get all user's payments")
    public Page<PaymentDto> getAllPayments(
            Pageable pageable,
            Authentication authentication,
            @RequestParam(value = "user_id", required = false) Long userId) {
        User curentUser = (User) authentication.getPrincipal();
        boolean isAdmin = curentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals(Role.RoleName.ADMIN));

        return paymentService.findAll(isAdmin ? userId : curentUser.getId(), pageable);
    }

    @PostMapping("/{orderId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new payment", description = "Create Stripe session for payment")
    public PaymentDto createPayment(
            @PathVariable @Positive Long orderId,
            UriComponentsBuilder uriBuilder)
            throws StripeException {
        return paymentService.createPayment(orderId, uriBuilder);
    }

    @GetMapping("/success")
    @Operation(summary = "Mark successful payment", description = "Change payment status")
    @ResponseStatus(HttpStatus.CREATED)
    public void successPayment(@RequestParam("session_id") String sessionId)
            throws StripeException {
        Session session = Session.retrieve(sessionId);
        paymentService.success(session);
    }

    @GetMapping("/cancel")
    @Operation(summary = "Mark canceled payment", description = "Change payment status")
    public ResponseEntity<String> cancelPayment(@RequestParam("session_id") String sessionId)
            throws StripeException {
        Session session = Session.retrieve(sessionId);
        return ResponseEntity.ok(paymentService.cancel(session));
    }

    @PutMapping("/renew/{paymentId}")
    @Operation(summary = "Renew payment", description = "Create new session for payment")
    public PaymentDto renewPayment(@PathVariable Long paymentId,
                                   UriComponentsBuilder uriComponentsBuilder) {
        return paymentService.renewPaymentSession(paymentId, uriComponentsBuilder);
    }
}
