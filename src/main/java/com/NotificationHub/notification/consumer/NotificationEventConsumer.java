package com.NotificationHub.notification.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.NotificationHub.notification.entity.NotificationEvent;
import com.NotificationHub.notification.processor.NotificationProcessor;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventConsumer {

    private final NotificationProcessor notificationProcessor;

    @KafkaListener(
            topics = "${notifyhub.kafka.topic:notification-events}",
            groupId = "${notifyhub.kafka.consumer-group:notification-service}"
    )
    public void consume(NotificationEvent event) {

        log.info(
                "Received notification event from Kafka. eventId={}",
                event.getId()
        );

        notificationProcessor.process(event);

        log.info(
                "Notification event processed successfully. eventId={}",
                event.getId()
        );
    }
}
