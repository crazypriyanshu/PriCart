package com.pdas.priCart.shop.order.service;

import com.pdas.priCart.shop.cartAndCheckout.dtos.PaymentInitializationResponse;
import com.pdas.priCart.shop.cartAndCheckout.dtos.PaymentWebhookDto;
import com.pdas.priCart.shop.order.dto.OrderDto;
import com.pdas.priCart.shop.order.models.Order;

import java.util.List;

public interface OrderService {
    OrderDto placeOrder();
    OrderDto getOrder(Long orderId);

    List<OrderDto> getOrderByUserId(Long userId);

    void completeOrder(PaymentWebhookDto dto);

    OrderDto convertToDto(Order order);

    OrderDto checkout(Long orderId);
    PaymentInitializationResponse initiatePayment(Long orderId, String gateway);
    void handlePaymentSuccess(PaymentWebhookDto dto);
    void handlePaymentFailure(PaymentWebhookDto dto);
    void handlePaymentCancelled(PaymentWebhookDto dto);

}
