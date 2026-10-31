package com.NotificationHub.notification.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.NotificationHub.exception.NotificationNotFoundException;
import com.NotificationHub.notification.dto.NotificationRequest;
import com.NotificationHub.notification.dto.NotificationResponse;
import com.NotificationHub.notification.entity.NotificationEvent;
import com.NotificationHub.notification.enums.NotificationPriority;
import com.NotificationHub.notification.enums.NotificationStatus;
import com.NotificationHub.notification.producer.NotificationEventProducer;
import com.NotificationHub.notification.repository.NotificationEventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationEventRepository notificationEventRepository;

    private final NotificationEventProducer notificationEventProducer;

    @Override
    public NotificationResponse createNotification(NotificationRequest request) {

        var event = NotificationEvent.builder()
                .eventType(request.getEventType())
                .targetUserId(request.getTargetUserId())
                .payload(request.getPayload())
                .priority(
                        request.getPriority() != null
                                ? request.getPriority()
                                : NotificationPriority.NORMAL
                )
                .channels(request.getChannels())
                .status(NotificationStatus.PENDING)
                .createdAt(Instant.now())
                .build();

        var savedEvent = notificationEventRepository.save(event);

        notificationEventProducer.publish(savedEvent);

        return NotificationResponse.from(savedEvent);
    }

    @Override
    public NotificationResponse getNotification(String id) {

        var event = notificationEventRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new NotificationNotFoundException(id));

        return NotificationResponse.from(event);
    }
}