package com.NotificationHub.notification.service;


import com.NotificationHub.notification.dto.NotificationRequest;
import com.NotificationHub.notification.dto.NotificationResponse;


public interface NotificationService{

    NotificationResponse createNotification(NotificationRequest request);

    NotificationResponse getNotification(String id);
}