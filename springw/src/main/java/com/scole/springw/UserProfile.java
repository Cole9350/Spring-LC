package com.scole.springw;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "user_profiles")
public class UserProfile {

    @Id
    private String username;
    private List<SubmissionStat> submitStats;
    private LocalDateTime createdAt;  // Field for TTL

    // Default constructor
    public UserProfile() {
        this.createdAt = LocalDateTime.now();  // Automatically set the creation time when a new object is created
    }

    // Parameterized constructor
    public UserProfile(String username, List<SubmissionStat> submitStats) {
        this.username = username;
        this.submitStats = submitStats;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<SubmissionStat> getSubmitStats() {
        return submitStats;
    }

    public void setSubmitStats(List<SubmissionStat> submitStats) {
        this.submitStats = submitStats;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

class SubmissionStat {
    private String difficulty;
    private int count;

    // Default constructor
    public SubmissionStat() {}

    // Parameterized constructor
    public SubmissionStat(String difficulty, int count) {
        this.difficulty = difficulty;
        this.count = count;
    }

    // Getters and setters
    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
