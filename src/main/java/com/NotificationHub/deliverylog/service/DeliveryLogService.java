package com.NotificationHub.deliverylog.service;

import com.NotificationHub.deliverylog.entity.DeliveryLog;

public interface DeliveryLogService {

     DeliveryLog startAttempt(
            String eventId,
            String channel
    );

    void markSent(
            String eventId,
            String channel
    );

    void markFailed(
            String eventId,
            String channel,
            Exception exception
    );

    void markDeadLettered(
            String eventId
    );

    boolean isAlreadyDelivered(
            String eventId,
            String channel
    );
    
}
