package com.wrap.it.mapper;

import com.wrap.it.config.MapperConfig;
import com.wrap.it.dto.item.order.OrderItemDto;
import com.wrap.it.dto.order.OrderDto;
import com.wrap.it.model.Order;
import com.wrap.it.model.OrderItem;
import com.wrap.it.model.ShoppingCart;
import com.wrap.it.model.User;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", expression = "java(mapOrderItemsToDto(order.getOrderItems()))")
    OrderDto toDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "shippingAddress", source = "shippingAddress")
    @Mapping(target = "orderItems", expression = "java(mapOrderItems(shoppingCart, order))")
    @Mapping(target = "total",
            expression = "java(calculateTotal(mapOrderItems(shoppingCart, order)))")
    Order toOrder(ShoppingCart shoppingCart, String shippingAddress, User user);

    @Mapping(target = "orderItemId", source = "id")
    @Mapping(target = "itemId", source = "item.id")
    OrderItemDto toOrderItemDto(OrderItem orderItem);

    default Set<OrderItem> mapOrderItems(ShoppingCart shoppingCart, Order order) {
        return shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setItem(cartItem.getItem());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getItem().getPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toSet());
    }

    default BigDecimal calculateTotal(Set<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    default List<OrderItemDto> mapOrderItemsToDto(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toOrderItemDto)
                .toList();
    }
}
