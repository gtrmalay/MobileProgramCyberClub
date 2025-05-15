package com.example.tdsclub.models;

import com.google.gson.annotations.SerializedName;

public class Computer {
    @SerializedName("computer_id")
    private int computerId;

    @SerializedName("computer_number")
    private String computerNumber;

    @SerializedName("is_free")
    private boolean isFree;

    // Геттеры
    public int getComputerId() { return computerId; }
    public String getComputerNumber() { return computerNumber; }
    public boolean isFree() { return isFree; }
}