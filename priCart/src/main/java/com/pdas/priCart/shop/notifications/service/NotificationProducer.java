package com.pdas.priCart.shop.notifications.service;

import com.pdas.priCart.shop.notifications.events.PaymentNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {
    private final KafkaTemplate<String, PaymentNotificationEvent> kafkaTemplate;
    private static final String TOPIC = "payment-notifications";

    public void sendNotification(PaymentNotificationEvent event){
        log.info("Sending payment notification event to Kafka for Order: {}", event.orderId());
        kafkaTemplate.send(TOPIC, String.valueOf(event.orderId()), event);
    }
}
