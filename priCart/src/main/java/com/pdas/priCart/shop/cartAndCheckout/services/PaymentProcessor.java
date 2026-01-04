package com.pdas.priCart.shop.cartAndCheckout.services;

import com.pdas.priCart.shop.cartAndCheckout.dtos.PaymentInitializationResponse;
import com.pdas.priCart.shop.order.models.Order;
import com.pdas.priCart.shop.order.models.PaymentDetails;

public interface PaymentProcessor {
    PaymentInitializationResponse initiatePayment(Order order);
    String setUpGateway();
    PaymentDetails verifyPayment(String gatewayOrderId);
}

