package com.example.tdsclub.models;

import com.google.gson.annotations.SerializedName;

public class ActiveBooking {
    @SerializedName("computer_number")
    private String computerNumber;

    @SerializedName("start_time")
    private String startTime;

    @SerializedName("end_time")
    private String endTime;

    @SerializedName("booking_date")
    private String bookingDate;

    // Геттеры
    public String getComputerNumber() { return computerNumber; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getBookingDate() { return bookingDate; }
}