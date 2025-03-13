package com.wrap.it.service.impl;

import com.wrap.it.dto.item.order.OrderItemDto;
import com.wrap.it.dto.order.ChangeOrderStatusRequestDto;
import com.wrap.it.dto.order.OrderDto;
import com.wrap.it.dto.order.PlaceOrderRequestDto;
import com.wrap.it.exception.EntityNotFoundException;
import com.wrap.it.mapper.OrderItemMapper;
import com.wrap.it.mapper.OrderMapper;
import com.wrap.it.model.Order;
import com.wrap.it.model.OrderItem;
import com.wrap.it.model.ShoppingCart;
import com.wrap.it.model.User;
import com.wrap.it.repository.OrderItemRepository;
import com.wrap.it.repository.OrderRepository;
import com.wrap.it.repository.ShoppingCartRepository;
import com.wrap.it.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderDto saveOrder(PlaceOrderRequestDto requestDto, User user) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .getCartByUserId(user.getId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find user's shopping cart"));
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new RuntimeException("User with id " + user.getId()
                    + ", has not items in cart");
        }
        return orderMapper.toDto(orderRepository.save(orderMapper.toOrder(shoppingCart,
                requestDto.getShippingAddress(), user)));
    }

    @Override
    public Page<OrderDto> findOrders(Pageable pageable, Long userId) {
        return orderRepository.findAllByUserId(userId, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public OrderDto changeStatus(Long orderId, ChangeOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException("Can't update order with id "
                        + orderId + " because it does not exist")
        );
        order.setStatus(requestDto.status());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Page<OrderItemDto> findItemsByOrderId(Long orderId, Pageable pageable, Long userId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found or does not belong to the user"));
        List<OrderItemDto> orderItemDto = order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
        return new PageImpl<>(orderItemDto, pageable, orderItemDto.size());
    }

    @Override
    public OrderItemDto findItemFormOrder(Long orderId, Long itemId, Long userId) {
        OrderItem orderItem = orderItemRepository
                .findByIdAndOrderIdAndUserId(itemId, orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order item not found or does not belong to the user"));
        return orderItemMapper.toDto(orderItem);
    }
}
