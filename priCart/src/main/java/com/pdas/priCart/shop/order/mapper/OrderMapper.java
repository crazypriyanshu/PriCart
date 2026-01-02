package com.pdas.priCart.shop.order.mapper;


import com.pdas.priCart.shop.order.dto.OrderDto;
import com.pdas.priCart.shop.order.models.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderStatus", target = "status")
    OrderDto toDto(Order order);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "status", target = "orderStatus")
    Order toEntity(OrderDto orderDto);


}
