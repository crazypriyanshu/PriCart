package com.pdas.priCart.shop.cartAndCheckout.controllers;

import com.pdas.priCart.shop.cartAndCheckout.dtos.PaymentInitializationResponse;
import com.pdas.priCart.shop.cartAndCheckout.services.CheckoutService;
import com.pdas.priCart.shop.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api_prefix}/checkout")
public class CheckoutController {
    @Autowired
    private final CheckoutService checkoutService;

    @PostMapping("/initiate-payment")
    public ResponseEntity<ApiResponse> initiatePayment(@RequestParam Long orderId, @RequestParam String gateway){
        PaymentInitializationResponse response = checkoutService.checkout(orderId, gateway);
        return ResponseEntity.ok().body(new ApiResponse<>("Payment initialization success", response));
    }
}
