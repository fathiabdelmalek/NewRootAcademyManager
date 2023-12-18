package com.fathi.newrootacademymanager.models;

import com.fathi.newrootacademymanager.helpers.enums.ActivityType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime timestamp;
    @Column(nullable = false)
    private ActivityType type;
    @Column(nullable = false)
    private String details;

    public Activity() {
        this.timestamp = LocalDateTime.now();
    }

    public Activity(ActivityType type, String details) {
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return type + ": " + details;
    }
}
