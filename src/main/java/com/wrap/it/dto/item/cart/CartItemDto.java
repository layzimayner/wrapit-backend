package com.wrap.it.dto.item.cart;

import lombok.Data;

@Data
public class CartItemDto {
    private Long cartItemId;
    private Long itemId;
    private String name;
    private Integer quantity;
    private String description;
}
