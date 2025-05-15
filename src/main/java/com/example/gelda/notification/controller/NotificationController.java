package com.example.gelda.notification.controller;

import com.example.gelda.notification.entity.Notification;
import com.example.gelda.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.gelda.notification.dto.NotificationDTO;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // ✅ Get all notifications for a user


    @GetMapping("/user/{userId}")
    public List<NotificationDTO> getUserNotifications(@PathVariable Long userId) {
        return notificationService.getUserNotificationDTOs(userId);
    }


    // ✅ Mark one notification as seen
    @PutMapping("/{notificationId}/seen")
    public String markAsSeen(@PathVariable Long notificationId) {
        notificationService.markAsSeen(notificationId);
        return "Notification marked as seen";
    }


    @GetMapping("/user/{userId}/unseen")
    public List<NotificationDTO> getUnseenNotifications(@PathVariable Long userId) {
        return notificationService.getUnseenNotificationDTOs(userId);
    }

}
