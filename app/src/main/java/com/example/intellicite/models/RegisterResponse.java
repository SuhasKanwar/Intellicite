package com.example.intellicite.models;

public class RegisterResponse {
    private boolean success;
    private String message;
    private String userID;

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getUserID() {
        return userID;
    }
}
