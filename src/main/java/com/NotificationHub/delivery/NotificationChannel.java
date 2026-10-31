package com.NotificationHub.delivery;

import com.NotificationHub.notification.entity.NotificationEvent;
import com.NotificationHub.template.entity.NotificationTemplate;

public interface NotificationChannel {

    String getChannelType();

    void send(
            NotificationEvent event,
            NotificationTemplate template
    );
}