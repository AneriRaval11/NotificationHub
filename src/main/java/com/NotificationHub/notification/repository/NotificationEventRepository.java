package com.NotificationHub.notification.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.NotificationHub.notification.entity.NotificationEvent;
import com.NotificationHub.notification.enums.NotificationStatus;

@Repository 
public interface NotificationEventRepository extends
 MongoRepository<NotificationEvent, String>{

    List<NotificationEvent> findByStatus( NotificationStatus status);
    
}
