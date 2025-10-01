package com.example.voting.data.model;

public class Poll {
    private long id;
    private String title;
    private String description;
    private String optionsJson; // Store options as JSON string
    private long endTimeMillis;

    public Poll(long id, String title, String description, String optionsJson, long endTimeMillis) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.optionsJson = optionsJson;
        this.endTimeMillis = endTimeMillis;
    }

    public Poll(String title, String description, String optionsJson, long endTimeMillis) {
        this(-1, title, description, optionsJson, endTimeMillis);
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getOptionsJson() { return optionsJson; }
    public long getEndTimeMillis() { return endTimeMillis; }
} 