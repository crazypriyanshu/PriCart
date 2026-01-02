package com.pdas.priCart.shop.order.service;

import com.pdas.priCart.shop.cartAndCheckout.dtos.PaymentWebhookDto;
import com.pdas.priCart.shop.order.dto.OrderDto;
import com.pdas.priCart.shop.order.models.Order;

import java.util.List;

public interface OrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getOrderByUserId(Long userId);

    void completeOrder(PaymentWebhookDto dto);

    OrderDto convertToDto(Order order);

}
