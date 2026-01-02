package com.pdas.priCart.shop.cartAndCheckout.repositories;

import com.pdas.priCart.shop.cartAndCheckout.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
}
