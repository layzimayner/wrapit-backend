package com.wrap.it.dto.cart;

import com.wrap.it.dto.item.cart.CartItemDto;
import java.util.Set;
import lombok.Data;

@Data
public class CartDto {
    private Long userId;
    private Long cartId;
    private Set<CartItemDto> cartItems;
}
