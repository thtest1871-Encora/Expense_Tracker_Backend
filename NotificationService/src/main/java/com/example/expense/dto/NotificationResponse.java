package com.example.expense.dto;

import java.sql.Timestamp;

public class NotificationResponse {

    private Long id;
    private Long userId;
    private String type;
    private String message;
    private Timestamp createdAt;

    public NotificationResponse() {}

    public NotificationResponse(Long id, Long userId, String type, String message, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.message = message;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "NotificationResponse{" +
                "id=" + id +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
