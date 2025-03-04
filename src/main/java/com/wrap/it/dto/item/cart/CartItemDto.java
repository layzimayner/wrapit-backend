package com.wrap.it.dto.item.cart;

import lombok.Data;

@Data
public class CartItemDto {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private Integer quantity;
}
