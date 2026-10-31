package com.NotificationHub.notification.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.NotificationHub.delivery.ChannelRouter;
import com.NotificationHub.deliverylog.service.DeliveryLogService;
import com.NotificationHub.notification.entity.NotificationEvent;
import com.NotificationHub.template.service.TemplateResolver;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProcessor {

    private final TemplateResolver templateResolver;

    private final ChannelRouter channelRouter;

    private final DeliveryLogService deliveryLogService;

    public void process(NotificationEvent event) {

        log.info("Processing notification. eventId={}, channels={}",event.getId(),event.getChannels());

        for (String channelType : event.getChannels()) {

            processChannel(event, channelType);
        }
    }

    private void processChannel(NotificationEvent event, String channelType) {

        var eventId = event.getId();

        log.info("Processing channel. eventId={}, channel={}", eventId, channelType);

        /*
         * Idempotency check.
         *
         * If Kafka redelivers the event after
         * successful delivery, don't send again.
         */
        if (deliveryLogService.isAlreadyDelivered(eventId,channelType)) {

            log.info("Notification already delivered. " + "Skipping duplicate. eventId={}, channel={}",eventId, channelType);

            return;
        }

        deliveryLogService.startAttempt(eventId, channelType);

        try {

            var template = templateResolver.resolve(event.getEventType(), channelType);

            var channel = channelRouter.getChannel(channelType);

            channel.send(event, template);

            deliveryLogService.markSent(eventId, channelType);

            log.info("Notification delivered successfully. " + "eventId={}, channel={}", eventId, channelType);

        } catch (Exception exception) {

            deliveryLogService.markFailed(eventId, channelType, exception);

            log.error("Notification delivery failed. " + "eventId={}, channel={}", eventId, channelType, exception);

            /*
             * Important:
             * rethrow the exception so Kafka's
             * error handler can retry the event.
             */
            throw exception;
        }
    }
}