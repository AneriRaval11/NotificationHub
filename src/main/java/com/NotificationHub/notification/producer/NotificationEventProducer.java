package com.NotificationHub.notification.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.NotificationHub.notification.entity.NotificationEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor 
public class NotificationEventProducer {

    public static final String NOTIFICATION_TOPIC =
            "notification-events";

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public void publish(NotificationEvent event) {

        kafkaTemplate.send(
                NOTIFICATION_TOPIC,
                event.getId(),
                event
        );
    }
}
