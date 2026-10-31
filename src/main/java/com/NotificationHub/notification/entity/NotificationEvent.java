package com.NotificationHub.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.NotificationHub.notification.enums.NotificationPriority;
import com.NotificationHub.notification.enums.NotificationStatus;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Document(collection = "notification_events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {

    @Id
    private String id;

    private String eventType;

    private String targetUserId;

    private Map<String, Object> payload;

    private NotificationPriority priority;

    private List<String> channels;

    @Indexed
    private NotificationStatus status;

    private Instant createdAt;
}