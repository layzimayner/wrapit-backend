package com.wrap.it.dto.item.order;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long orderItemId;
    private Long itemId;
    private int quantity;
}
