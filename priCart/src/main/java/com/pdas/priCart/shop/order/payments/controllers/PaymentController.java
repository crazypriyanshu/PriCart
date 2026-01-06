package com.pdas.priCart.shop.order.payments.controllers;

import com.pdas.priCart.shop.common.dto.ApiResponse;
import com.pdas.priCart.shop.order.payments.dtos.PaymentVerifyRequest;
import com.pdas.priCart.shop.order.payments.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentVerificationService;

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyPayment(@RequestBody PaymentVerifyRequest request){
        paymentVerificationService.verifyPayment(request);
        return ResponseEntity.ok().body(new ApiResponse<>("Payment verified successfully", null));
    }
}
