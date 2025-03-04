package com.wrap.it.dto.item.cart;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateCartItemRequestDto {
    @Positive(message = "Quantity must be at least 1")
    private int quantity;
}
