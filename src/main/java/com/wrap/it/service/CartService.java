package com.wrap.it.service;

import com.wrap.it.dto.cart.CartDto;
import com.wrap.it.dto.item.cart.CartItemDto;
import com.wrap.it.dto.item.cart.CreateCartItemRequestDto;
import com.wrap.it.dto.item.cart.UpdateCartItemRequestDto;
import com.wrap.it.model.User;

public interface CartService {
    CartDto getCart(Long userId);

    CartItemDto addItemToCart(CreateCartItemRequestDto requestDro,
                              User user);

    CartItemDto updateItemQuantity(Long id,
                                   UpdateCartItemRequestDto requestDro,
                                   User user);

    void deleteItemFromCart(Long id,
                            User user);

    void createShoppingCartForUser(User user);
}
