package com.NotificationHub.notification.controller;



import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.NotificationHub.notification.dto.NotificationRequest;
import com.NotificationHub.notification.dto.NotificationResponse;
import com.NotificationHub.notification.service.NotificationService;

import java.net.URI;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

  
    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody NotificationRequest request) {

        NotificationResponse response =
                notificationService.createNotification(request);

        return ResponseEntity
                .accepted()
                .location(
                        URI.create(
                                "/api/notifications/"
                                        + response.getNotificationId()
                        )
                )
                .body(response);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotification(
            @PathVariable String id) {

        return ResponseEntity.ok(
                notificationService.getNotification(id)
        );
    }
}