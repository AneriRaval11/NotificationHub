package com.NotificationHub.deliverylog.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.NotificationHub.deliverylog.enums.DeliveryStatus;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "delivery_logs")
@CompoundIndex(
        name = "event_channel_idx",
        def = "{'eventId': 1, 'channel': 1}",
        unique = true
)
public class DeliveryLog {

    @Id
    private String id;

    private String eventId;

    private String channel;

    private DeliveryStatus status;

    private int attemptCount;

    private String lastError;

    private Instant attemptedAt;

    private boolean deadLettered;
}