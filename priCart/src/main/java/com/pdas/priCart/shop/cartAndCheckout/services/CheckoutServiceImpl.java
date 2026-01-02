package com.pdas.priCart.shop.cartAndCheckout.services;

import com.pdas.priCart.shop.cartAndCheckout.dtos.PaymentInitializationResponse;
import com.pdas.priCart.shop.order.models.Order;
import com.pdas.priCart.shop.order.models.OrderStatus;
import com.pdas.priCart.shop.order.orderRepository.OrderRepository;
import com.pdas.priCart.shop.product.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService{
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;


    @Override
    public PaymentInitializationResponse checkout(Long orderId, String gateway) {
        // fetch the order:
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException(" Order not found with ID: "+orderId));

        // check if order is pending state
        if (order.getOrderStatus() != OrderStatus.PENDING){
            throw new IllegalArgumentException("To process order, Order should be on pending state, but current state of order is: "+order.getOrderStatus());
        }

        // STrategy pattern
        return paymentService.processPayment(order, gateway);
    }
}
