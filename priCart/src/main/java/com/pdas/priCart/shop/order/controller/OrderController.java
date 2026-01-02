package com.pdas.priCart.shop.order.controller;


import com.pdas.priCart.shop.common.dto.ApiResponse;
import com.pdas.priCart.shop.order.dto.OrderDto;
import com.pdas.priCart.shop.order.models.Order;
import com.pdas.priCart.shop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api_prefix}/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/user/{userId}/place-order")
    public ResponseEntity<ApiResponse> createOrder(@PathVariable Long userId){

        // place order handles validations
        Order order = orderService.placeOrder(userId);
        OrderDto orderDto = orderService.convertToDto(order);
        return ResponseEntity.ok().body(new ApiResponse<>("Order placed successfully", orderDto));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId){
        OrderDto orderDto = orderService.getOrder(orderId);
        return ResponseEntity.ok().body(new ApiResponse<>("Order found", orderDto));

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getOrdersByUserId(@PathVariable Long userId){
        List<OrderDto> orders = orderService.getOrderByUserId(userId);
        return ResponseEntity.ok().body(new ApiResponse<>("User orders ", orders));
    }
}
