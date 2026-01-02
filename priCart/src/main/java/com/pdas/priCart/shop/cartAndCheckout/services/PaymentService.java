package com.pdas.priCart.shop.cartAndCheckout.services;

import com.pdas.priCart.shop.cartAndCheckout.dtos.PaymentInitializationResponse;
import com.pdas.priCart.shop.order.models.Order;
import com.pdas.priCart.shop.order.models.OrderStatus;
import com.pdas.priCart.shop.order.models.PaymentDetails;
import com.pdas.priCart.shop.order.orderRepository.OrderRepository;
import com.pdas.priCart.shop.order.orderRepository.PaymentDetailsRepository;
import com.pdas.priCart.shop.product.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class PaymentService {
    private final Map<String, PaymentProcessor> paymentProcessorMap;
    private final PaymentDetailsRepository paymentDetailsRepository;


    public PaymentInitializationResponse processPayment(Order order, String gateway){
        PaymentProcessor paymentProcessor = paymentProcessorMap.get(gateway);
        if (paymentProcessor == null){
            throw new ResourceNotFoundException("Payment gateway '" + gateway + "' is not supported.");
        }
        PaymentInitializationResponse response = paymentProcessor.initiatePayment(order);


        // create payment link and
        PaymentDetails payment = new PaymentDetails();
        payment.setGatewayName(gateway);
        payment.setGatewayOrderId(response.getOrderId());
        payment.setStatus(OrderStatus.PENDING.name());
        payment.setOrder(order);
        order.setPaymentDetails(payment);
        paymentDetailsRepository.save(payment);

        return response;
    }
}
