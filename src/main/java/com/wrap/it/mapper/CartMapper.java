package com.wrap.it.mapper;

import com.wrap.it.config.MapperConfig;
import java.util.Set;
import java.util.stream.Collectors;

import com.wrap.it.dto.item.cart.CartItemDto;
import com.wrap.it.model.CartItem;
import com.wrap.it.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, componentModel = "spring")
public interface CartMapper {
    @Mapping(source = "shoppingCart.id", target = "userId")
    @Mapping(source = "shoppingCart.id", target = "cartId")
    CartDto toDto(ShoppingCart shoppingCart);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toCartItemDto(CartItem cartItem);

    default Set<CartItemDto> mapCartItemsToDto(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toCartItemDto)
                .collect(Collectors.toSet());
    }
}
