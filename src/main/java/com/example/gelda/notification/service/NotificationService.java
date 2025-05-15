package com.example.gelda.notification.service;

import com.example.gelda.notification.entity.Notification;
import com.example.gelda.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.gelda.notification.dto.NotificationDTO;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    @Autowired
    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public void createNotification(Long userId, String message) {
        Notification notification = new Notification(userId, message);
        repository.save(notification);
    }



    public List<NotificationDTO> getUserNotificationDTOs(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(n -> new NotificationDTO(
                        n.getId(),
                        n.getTitle(),
                        n.getMessage(),
                        n.isSeen()
                ))
                .toList();
    }


    public void markAsSeen(Long notificationId) {
        Notification notification = repository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setSeen(true);
        repository.save(notification);
    }


    public List<NotificationDTO> getUnseenNotificationDTOs(Long userId) {
        return repository.findByUserIdAndSeenFalse(userId).stream()
                .map(n -> new NotificationDTO(
                        n.getId(),
                        n.getTitle(),
                        n.getMessage(),
                        n.isSeen()
                ))
                .toList();
    }


}
