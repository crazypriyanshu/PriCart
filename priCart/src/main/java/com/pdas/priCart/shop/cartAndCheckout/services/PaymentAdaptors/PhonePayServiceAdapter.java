package com.pdas.priCart.shop.cartAndCheckout.services.PaymentAdaptors;

import com.pdas.priCart.shop.cartAndCheckout.dtos.PaymentInitializationResponse;
import com.pdas.priCart.shop.cartAndCheckout.models.PaymentType;
import com.pdas.priCart.shop.cartAndCheckout.services.PaymentProcessor;
import com.pdas.priCart.shop.order.models.Order;
import com.pdas.priCart.shop.order.models.PaymentDetails;

public class PhonePayServiceAdapter implements PaymentProcessor {


    @Override
    public PaymentInitializationResponse initiatePayment(Order order) {
        return null;
    }

    @Override
    public String setUpGateway() {
        return PaymentType.PHONE_PAY.toString();
    }

    @Override
    public PaymentDetails verifyPayment(String gatewayOrderId) {
        return null;
    }
}
