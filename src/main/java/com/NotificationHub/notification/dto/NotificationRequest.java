package com.NotificationHub.notification.dto;

import java.util.List;
import java.util.Map;

import com.NotificationHub.notification.enums.NotificationPriority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@Builder
@NoArgsConstructor 
@AllArgsConstructor  
public class NotificationRequest{
    
    @NotBlank(message = "eventType is required")
    private String eventType;

    @NotBlank(message = "targetUserId is required")
    private String targetUserId;

    @NotNull(message = "payload is required")
    private Map<String, Object> payload;

    private NotificationPriority priority;

    @NotEmpty(message = "At least one channel is required")
    private List<String> channels;
}
