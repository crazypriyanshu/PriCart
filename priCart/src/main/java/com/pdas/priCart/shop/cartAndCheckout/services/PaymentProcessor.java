package com.pdas.priCart.shop.cartAndCheckout.services;

import com.pdas.priCart.shop.cartAndCheckout.dtos.PaymentInitializationResponse;
import com.pdas.priCart.shop.cartAndCheckout.models.PaymentType;
import com.pdas.priCart.shop.order.models.Order;

public interface PaymentProcessor {
    PaymentInitializationResponse initiatePayment(Order order);
    String setUpGateway();
}

