package com.NotificationHub.template.service;

import com.NotificationHub.template.entity.NotificationTemplate;

public interface TemplateResolver {

    NotificationTemplate resolve(
            String eventType,
            String channel
    );
}