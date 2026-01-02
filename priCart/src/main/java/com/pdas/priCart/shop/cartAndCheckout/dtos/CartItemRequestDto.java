package com.pdas.priCart.shop.cartAndCheckout.dtos;

import com.pdas.priCart.shop.product.dto.ProductDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemRequestDto {
    private Long itemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDto product;
}
