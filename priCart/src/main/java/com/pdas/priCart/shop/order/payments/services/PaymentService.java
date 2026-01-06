package com.pdas.priCart.shop.order.payments.services;

import com.pdas.priCart.shop.order.payments.dtos.PaymentVerifyRequest;

public interface PaymentService {
    void verifyPayment(PaymentVerifyRequest request);
}
