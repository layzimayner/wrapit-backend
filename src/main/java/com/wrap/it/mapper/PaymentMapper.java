package com.wrap.it.mapper;

import com.wrap.it.config.MapperConfig;
import com.wrap.it.dto.payment.PaymentDto;
import com.wrap.it.model.Order;
import com.wrap.it.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    @Mapping(target = "status", ignore = true)
    Payment toModel(Order order);

    PaymentDto toDto(Payment payment);
}
