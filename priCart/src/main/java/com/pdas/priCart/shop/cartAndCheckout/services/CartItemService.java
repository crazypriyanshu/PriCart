package com.pdas.priCart.shop.cartAndCheckout.services;

import com.pdas.priCart.shop.cartAndCheckout.models.Cart;
import com.pdas.priCart.shop.cartAndCheckout.models.CartItem;
import com.pdas.priCart.shop.product.exception.ProductNotFoundException;

public interface CartItemService {
    Cart addCartItem(Long cartId, Long productId, int quantity) throws ProductNotFoundException;
    Cart removeCartItem(Long productId);
    Cart updateItemQuantity(Long productId, int quantity);
    CartItem getCartItem(Long productId);

}
