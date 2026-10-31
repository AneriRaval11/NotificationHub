package com.NotificationHub.config;

import com.NotificationHub.deliverylog.service.DeliveryLogService;
import com.NotificationHub.notification.entity.NotificationEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.apache.kafka.common.header.Headers;


public class NotifyHubDeadLetterRecoverer
        extends DeadLetterPublishingRecoverer {

    private final DeliveryLogService deliveryLogService;

    public NotifyHubDeadLetterRecoverer(
            KafkaOperations<String, NotificationEvent>
                    template,
            DeliveryLogService deliveryLogService
    ) {

        super(
                template,
                (record, exception) ->
                        new TopicPartition(
                                record.topic() + ".DLT",
                                record.partition()
                        )
        );

        this.deliveryLogService =
                deliveryLogService;
    }

    @Override
    protected ProducerRecord<Object, Object>
    createProducerRecord(
            ConsumerRecord<?, ?> record,
            TopicPartition topicPartition,
            Headers headers,
            byte[] keyBytes,
            byte[] valueBytes
    ) {

        if (record.value()
                instanceof NotificationEvent event) {

            deliveryLogService.markDeadLettered(
                    event.getId()
            );
        }

        return super.createProducerRecord(
                record,
                topicPartition,
                headers,
                keyBytes,
                valueBytes
        );
    }
}