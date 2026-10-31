package com.NotificationHub.template.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.NotificationHub.template.entity.NotificationTemplate;

import java.util.Optional;

public interface NotificationTemplateRepository extends MongoRepository<NotificationTemplate, String> {

    Optional<NotificationTemplate>
    findFirstByEventTypeAndChannelOrderByVersionDesc(
            String eventType,
            String channel
    );
}