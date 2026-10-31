package com.NotificationHub.template.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "templates")
@CompoundIndex(
        name = "eventType_channel_version_idx",
        def = "{'eventType': 1, 'channel': 1, 'version': -1}"
)
public class NotificationTemplate {

    @Id
    private String id;

    private String eventType;

    private String channel;

    private String subject;

    private String body;

    private Integer version;

    private Instant updatedAt;
}
