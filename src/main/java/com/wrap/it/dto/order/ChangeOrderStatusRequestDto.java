package com.wrap.it.dto.order;

import com.example.demo.model.Order;
import jakarta.validation.constraints.NotNull;

public record ChangeOrderStatusRequestDto(
        @NotNull
        Order.Status status) {
}
