package org.example.model;

import java.time.LocalDateTime;

public class Comment {
    private String userId;
    private String text;
    private LocalDateTime date;

    public Comment(String userId, String text) {
        this.userId = userId;
        this.text = text;
        this.date = LocalDateTime.now();
    }

    public Comment(String userId, String text,  LocalDateTime createdAt) {
        this.userId = userId;
        this.text = text;
        this.date = createdAt;
    }

    public String getUser() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public Object getCreatedAt() {
        return date;
    }
}
