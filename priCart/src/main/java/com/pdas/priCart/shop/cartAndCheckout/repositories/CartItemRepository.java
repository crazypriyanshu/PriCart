package com.pdas.priCart.shop.cartAndCheckout.repositories;

import com.pdas.priCart.shop.cartAndCheckout.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCartId(Long cartId);
}
