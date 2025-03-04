package com.wrap.it.service;

import com.wrap.it.dto.item.order.OrderItemDto;
import com.wrap.it.dto.order.ChangeOrderStatusRequestDto;
import com.wrap.it.dto.order.OrderDto;
import com.wrap.it.dto.order.PlaceOrderRequestDto;
import com.wrap.it.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto saveOrder(PlaceOrderRequestDto requestDto, User user);

    Page<OrderDto> findOrders(Pageable pageable, Long userId);

    OrderDto changeStatus(Long orderId, ChangeOrderStatusRequestDto requestDto);

    Page<OrderItemDto> findItemsByOrderId(Long orderId, Pageable pageable, Long userId);

    OrderItemDto findItemFormOrder(Long orderId, Long itemId, Long userId);
}
