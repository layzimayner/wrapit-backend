package com.wrap.it.dto.payment;

import com.wrap.it.model.Payment;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentDto {
    private Long paymentId;
    private Payment.Status status;
    private BigDecimal total;
    private String sessionUrl;
    private String sessionId;
}
