package com.example.to_doapplication.data;

import com.google.firebase.database.PropertyName;
import java.util.UUID;

public class Task {
    private String id;
    private String title;
    private String description;
    private boolean isCompleted;
    private String userId;
    private long timestamp;
    private String categoryId;
    private Integer priority;
    private Long dueDate;
    private Long dueTime;

    public Task() {
        this.id = UUID.randomUUID().toString();
        this.title = "";
        this.description = "";
        this.isCompleted = false;
        this.userId = "";
        this.timestamp = System.currentTimeMillis();
        this.categoryId = null;
        this.priority = null;
        this.dueDate = null;
        this.dueTime = null;
    }

    public Task(String id, String title, String description, boolean isCompleted, String userId, long timestamp, String categoryId, Integer priority, Long dueDate, Long dueTime) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
        this.userId = userId;
        this.timestamp = timestamp;
        this.categoryId = categoryId;
        this.priority = priority;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @PropertyName("completed")
    public boolean isCompleted() { return isCompleted; }

    @PropertyName("completed")
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public Long getDueDate() { return dueDate; }
    public void setDueDate(Long dueDate) { this.dueDate = dueDate; }

    public Long getDueTime() { return dueTime; }
    public void setDueTime(Long dueTime) { this.dueTime = dueTime; }
}
