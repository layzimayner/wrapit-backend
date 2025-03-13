package com.wrap.it.mapper;

import com.wrap.it.config.MapperConfig;
import com.wrap.it.dto.item.order.OrderItemDto;
import com.wrap.it.model.OrderItem;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "orderItemId", source = "id")
    @Mapping(target = "itemId", source = "item.id")
    OrderItemDto toDto(OrderItem orderItem);

    List<OrderItemDto> toDtos(List<OrderItem> orderItems);
}
