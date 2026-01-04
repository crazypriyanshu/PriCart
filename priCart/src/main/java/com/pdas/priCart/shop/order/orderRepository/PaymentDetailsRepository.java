package com.pdas.priCart.shop.order.orderRepository;

import com.pdas.priCart.shop.order.models.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {
    // This will work because PaymentDetails HAS this field
    Optional<PaymentDetails> findByGatewayOrderId(String gatewayOrderId);
    Optional<PaymentDetails> findByOrderId(Long orderId);
}
