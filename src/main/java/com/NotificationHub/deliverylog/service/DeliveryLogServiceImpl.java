package com.NotificationHub.deliverylog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.NotificationHub.deliverylog.entity.DeliveryLog;
import com.NotificationHub.deliverylog.enums.DeliveryStatus;
import com.NotificationHub.deliverylog.repository.DeliveryLogRepository;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class DeliveryLogServiceImpl
        implements DeliveryLogService {

    private final DeliveryLogRepository deliveryLogRepository;

    @Override
    public DeliveryLog startAttempt(String eventId, String channel) {

        var deliveryLog = deliveryLogRepository.findByEventIdAndChannel(
                                eventId, channel)
                        .orElseGet(() ->
                                DeliveryLog.builder()
                                        .eventId(eventId)
                                        .channel(channel)
                                        .attemptCount(0)
                                        .deadLettered(false)
                                        .build());

        deliveryLog.setStatus(DeliveryStatus.PROCESSING);

        deliveryLog.setAttemptCount(deliveryLog.getAttemptCount() + 1);

        deliveryLog.setAttemptedAt(Instant.now());

        deliveryLog.setLastError(null);

        return deliveryLogRepository.save(deliveryLog);
    }

    @Override
    public void markSent(String eventId, String channel) {

        var deliveryLog = getDeliveryLog(eventId, channel);

        deliveryLog.setStatus(DeliveryStatus.SENT);

        deliveryLog.setAttemptedAt(Instant.now());

        deliveryLog.setLastError(null);

        deliveryLogRepository.save(deliveryLog);
    }

    @Override
    public void markFailed( String eventId, String channel, Exception exception) {

        DeliveryLog deliveryLog = getDeliveryLog(eventId, channel);

        deliveryLog.setStatus(DeliveryStatus.FAILED);

        deliveryLog.setAttemptedAt(Instant.now());

        deliveryLog.setLastError(exception.getMessage());

        deliveryLogRepository.save(deliveryLog);
    }

    @Override
    public void markDeadLettered(String eventId) {

        deliveryLogRepository.findByEventId(eventId)
                .forEach(deliveryLog -> {

                    deliveryLog.setStatus(DeliveryStatus.DEAD_LETTERED);

                    deliveryLog.setDeadLettered(true);

                    deliveryLog.setAttemptedAt(Instant.now());

                    deliveryLogRepository.save(deliveryLog);
                });
    }

    @Override
    public boolean isAlreadyDelivered(String eventId, String channel) {

        return deliveryLogRepository.findByEventIdAndChannel(eventId, channel)
                .map(log -> log.getStatus() == DeliveryStatus.SENT)
                .orElse(false);
    }

    private DeliveryLog getDeliveryLog(String eventId, String channel) {

        return deliveryLogRepository
                .findByEventIdAndChannel(eventId, channel)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Delivery log not found. "
                                        + "eventId="
                                        + eventId
                                        + ", channel="
                                        + channel
                        )
                );
    }
}