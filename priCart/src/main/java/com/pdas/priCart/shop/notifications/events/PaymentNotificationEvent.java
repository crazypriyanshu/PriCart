package com.pdas.priCart.shop.notifications.events;

import java.math.BigDecimal;

public record PaymentNotificationEvent(
        Long orderId,
        String userEmail,
        String userName,
        BigDecimal amount,
        String status
) {
}
