package com.pdas.priCart.shop.order.orderRepository;

import com.pdas.priCart.shop.order.models.Order;
import com.pdas.priCart.shop.order.models.OrderStatus;
import com.pdas.priCart.shop.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    // Used by Webhook to find the order by Razorpay's ID
    Optional<Order> findByGatewayOrderId(String gatewayOrderId);

    // Finds an order for a user that matches any of the provided statuses
    Optional<Order> findByUserAndOrderStatusIn(User user, List<OrderStatus> statuses);

    Optional<Order> findTopByUserAndOrderStatusInOrderByCreatedAtDesc(
            User user,
            List<OrderStatus> statuses
    );
}
