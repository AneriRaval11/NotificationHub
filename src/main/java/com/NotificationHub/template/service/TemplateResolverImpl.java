package com.NotificationHub.template.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.NotificationHub.template.entity.NotificationTemplate;
import com.NotificationHub.template.repository.NotificationTemplateRepository;

@Service
@RequiredArgsConstructor
public class TemplateResolverImpl implements TemplateResolver {

    private final NotificationTemplateRepository templateRepository;

    @Override
    public NotificationTemplate resolve( String eventType,String channel) {

        return templateRepository
                .findFirstByEventTypeAndChannelOrderByVersionDesc(
                        eventType,
                        channel
                )
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No template found for eventType="
                                        + eventType
                                        + ", channel="
                                        + channel
                        )
                );
    }
}