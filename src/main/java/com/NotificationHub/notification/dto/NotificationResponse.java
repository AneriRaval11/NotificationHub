package com.NotificationHub.notification.dto;

import java.time.Instant;
import java.util.List;

import com.NotificationHub.notification.entity.NotificationEvent;
import com.NotificationHub.notification.enums.NotificationPriority;
import com.NotificationHub.notification.enums.NotificationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@Builder 
@AllArgsConstructor 
@NoArgsConstructor 
public class NotificationResponse {

     private String notificationId;

    private String eventType;

    private String targetUserId;

    private NotificationPriority priority;

    private List<String> channels;

    private NotificationStatus status;

    private Instant createdAt;

    public static NotificationResponse from(
            NotificationEvent event) {

        return NotificationResponse.builder()
                .notificationId(event.getId())
                .eventType(event.getEventType())
                .targetUserId(event.getTargetUserId())
                .priority(event.getPriority())
                .channels(event.getChannels())
                .status(event.getStatus())
                .createdAt(event.getCreatedAt())
                .build();
    }
    
}
