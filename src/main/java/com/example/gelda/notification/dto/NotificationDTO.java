package com.example.gelda.notification.dto;

public class NotificationDTO {

    private Long id;
    private String title;
    private String message;
    private boolean seen;

    public NotificationDTO(Long id, String title, String message, boolean seen) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.seen = seen;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
