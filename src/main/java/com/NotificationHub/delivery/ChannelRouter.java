package com.NotificationHub.delivery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ChannelRouter {

    private final Map<String, NotificationChannel> channels;

    public ChannelRouter(List<NotificationChannel> channelImplementations) {

        this.channels = channelImplementations.stream()
                .collect(Collectors.toMap(
                        NotificationChannel::getChannelType,
                        Function.identity()
                ));

        log.info(
                "Registered notification channels: {}",
                channels.keySet()
        );
    }

    public NotificationChannel getChannel(String channelType) {

        if (channelType == null || channelType.isBlank()) {
            throw new IllegalArgumentException(
                    "Channel type cannot be null or blank"
            );
        }

        NotificationChannel channel =
                channels.get(channelType.toUpperCase());

        if (channel == null) {
            throw new IllegalArgumentException(
                    "Unsupported notification channel: "
                            + channelType
            );
        }

        return channel;
    }
}