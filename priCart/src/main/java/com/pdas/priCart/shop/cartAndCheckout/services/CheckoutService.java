package com.pdas.priCart.shop.cartAndCheckout.services;

import com.pdas.priCart.shop.cartAndCheckout.dtos.PaymentInitializationResponse;

public interface CheckoutService {
    PaymentInitializationResponse checkout(Long orderId, String gateway);

    }

