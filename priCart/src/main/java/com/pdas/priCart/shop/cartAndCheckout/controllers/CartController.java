package com.pdas.priCart.shop.cartAndCheckout.controllers;

import com.pdas.priCart.shop.cartAndCheckout.dtos.CartAddResponse;
import com.pdas.priCart.shop.cartAndCheckout.mappers.CartMapper;
import com.pdas.priCart.shop.cartAndCheckout.models.Cart;
import com.pdas.priCart.shop.cartAndCheckout.services.CartService;
import com.pdas.priCart.shop.common.dto.ApiResponse;
import com.pdas.priCart.shop.product.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api_prefix}/carts")
public class CartController {
    @Autowired
    private final CartService cartService;
    private final CartMapper cartMapper;

    @GetMapping("/getUserCart")
    public ResponseEntity<ApiResponse> getUserCart(){
        try {
            Cart cart = cartService.getCart();
            CartAddResponse requestDto = cartMapper.toDto(cart);
            return ResponseEntity.ok().body(new ApiResponse<>("Success", requestDto));
        } catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @DeleteMapping("/clear-cart")
    public ResponseEntity<ApiResponse> clearCart(){
        try {
            cartService.clearCart();
            return ResponseEntity.ok().body(new ApiResponse<>("Cart cleared successfully", null));
        } catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(e.getMessage(), null));
        }

    }

    @GetMapping("/totalAmount")
    public ResponseEntity<ApiResponse> getTotalAmount(){
        try {
            BigDecimal totalAmount = cartService.getTotalPrice();
            return ResponseEntity.ok().body(new ApiResponse<>("Total Price: ", totalAmount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(e.getMessage(), null));
        }
    }
}
