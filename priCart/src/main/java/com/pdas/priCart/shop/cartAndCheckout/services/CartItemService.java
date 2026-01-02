package com.pdas.priCart.shop.cartAndCheckout.services;

import com.pdas.priCart.shop.cartAndCheckout.models.CartItem;
import com.pdas.priCart.shop.product.exception.ProductNotFoundException;

public interface CartItemService {
    void addCartItem(Long cartId, Long productId, int quantity) throws ProductNotFoundException;
    void removeCartItem(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);
    CartItem getCartItem(Long cartId, Long productId);
}
