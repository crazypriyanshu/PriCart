package com.pdas.priCart.shop.cartAndCheckout.services;

import com.pdas.priCart.shop.cartAndCheckout.models.Cart;
import com.pdas.priCart.shop.user.models.User;

import java.math.BigDecimal;
import java.util.Optional;

public interface CartService {
    Cart getCart(Long cartId);
    void clearCart(Long userId);
    BigDecimal getTotalPrice(Long cartId);
    Cart initializeNewCart(User user);
    Optional<Cart> getCartByUserId(Long userId);
}
