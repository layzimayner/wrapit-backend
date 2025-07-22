package com.wrap.it.mapper;

import com.wrap.it.config.MapperConfig;
import com.wrap.it.dto.cart.CartDto;
import com.wrap.it.dto.item.cart.CartItemDto;
import com.wrap.it.model.CartItem;
import com.wrap.it.model.Item;
import com.wrap.it.model.ShoppingCart;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, componentModel = "spring")
public interface CartMapper {
    @Mapping(source = "shoppingCart.id", target = "userId")
    @Mapping(source = "shoppingCart.id", target = "cartId")
    CartDto toDto(ShoppingCart shoppingCart);

    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "item.name", target = "name")
    @Mapping(source = "cartItem.id", target = "cartItemId")
    @Mapping(source = "cartItem.quantity", target = "quantity")
    CartItemDto toCartItemDto(CartItem cartItem, Item item);

    default Set<CartItemDto> mapCartItemsToDto(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(ci -> toCartItemDto(ci, ci.getItem()))
                .collect(Collectors.toSet());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "item", source = "item")
    @Mapping(target = "quantity", source = "quantity")
    CartItem toModel(ShoppingCart shoppingCart, Item item, short quantity);
}
