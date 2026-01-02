package com.pdas.priCart.shop.cartAndCheckout.services;

import com.pdas.priCart.shop.cartAndCheckout.dtos.CheckoutItemRequestDto;
import com.pdas.priCart.shop.cartAndCheckout.dtos.PaymentInitializationResponse;
import com.pdas.priCart.shop.cartAndCheckout.dtos.StripeResponseDto;

import java.util.List;

public interface CheckoutService {
    PaymentInitializationResponse checkout(Long orderId, String gateway);

    }

