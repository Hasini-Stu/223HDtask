package com.example.voting.data.model;

import java.util.List;
import java.util.Map;

public class Vote {
    private String voteId;
    private String title;
    private String description;
    private String creatorId;
    private long startTime;
    private long endTime;
    private List<String> options;
    private Map<String, Integer> results;
    private boolean isActive;
    private Map<String, String> translations; // For multilingual support

    public Vote() {
        // Required empty constructor for Firebase
    }

    public Vote(String voteId, String title, String description, String creatorId,
                long startTime, long endTime, List<String> options) {
        this.voteId = voteId;
        this.title = title;
        this.description = description;
        this.creatorId = creatorId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.options = options;
        this.isActive = true;
    }

    // Getters and Setters
    public String getVoteId() {
        return voteId;
    }

    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public Map<String, Integer> getResults() {
        return results;
    }

    public void setResults(Map<String, Integer> results) {
        this.results = results;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Map<String, String> getTranslations() {
        return translations;
    }

    public void setTranslations(Map<String, String> translations) {
        this.translations = translations;
    }
} 