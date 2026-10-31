package com.NotificationHub.deliverylog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.NotificationHub.deliverylog.entity.DeliveryLog;
import com.NotificationHub.deliverylog.enums.DeliveryStatus;

@Repository 
public interface DeliveryLogRepository extends MongoRepository<DeliveryLog, String> {
    Optional<DeliveryLog> findByEventIdAndChannel(
            String eventId,
            String channel
    );

    List<DeliveryLog> findByEventId(
            String eventId
    );

    List<DeliveryLog> findByStatus(
            DeliveryStatus status
    );
    
}
