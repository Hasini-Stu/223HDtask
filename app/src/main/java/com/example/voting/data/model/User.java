package com.example.voting.data.model;

public class User {
    private String userId;
    private String email;
    private String name;
    private boolean isAdmin;
    private String preferredLanguage;

    public User() {
        // Required empty constructor for Firebase
    }

    public User(String userId, String email, String name, boolean isAdmin, String preferredLanguage) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.isAdmin = isAdmin;
        this.preferredLanguage = preferredLanguage;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }
} 