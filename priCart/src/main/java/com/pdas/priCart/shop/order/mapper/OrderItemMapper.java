package com.pdas.priCart.shop.order.mapper;

import com.pdas.priCart.shop.order.dto.OrderItemDto;
import com.pdas.priCart.shop.order.models.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemDto toDto(OrderItem orderItem);
    OrderItem toEntity(OrderItemDto orderItemDto);
}
