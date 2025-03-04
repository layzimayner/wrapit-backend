package com.wrap.it.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.item.OrderItemDto;
import com.example.demo.dto.order.OrderDto;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toOrderItemDto(OrderItem orderItem);

    default Set<OrderItem> mapOrderItems(ShoppingCart shoppingCart, Order order) {
        return shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getBook().getPrice()
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
