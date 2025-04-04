package com.wrap.it.dto.item.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateCartItemRequestDto {
    @Positive
    @NotNull
    private Long itemId;
    @Positive(message = "Quantity must be at least 1")
    private short quantity;
}
