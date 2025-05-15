package com.example.tdsclub.models;

import com.google.gson.annotations.SerializedName;

public class CreateSessionRequest {
    @SerializedName("computer_number")
    private String computerNumber;

    @SerializedName("session_time")
    private String sessionTime;

    @SerializedName("duration_hours")
    private int durationHours;

    public CreateSessionRequest(String computerNumber, String sessionTime, int durationHours) {
        this.computerNumber = computerNumber;
        this.sessionTime = sessionTime;
        this.durationHours = durationHours;
    }
}