package com.example.tdsclub.models;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import android.util.Log;

public class SessionResponse {
    @SerializedName("client_id")
    private int id;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("computer_number")
    private String computerNumber;

    @SerializedName("session_time")
    private String sessionTime;

    @SerializedName("registration_date")
    private String registrationDate;

    // Геттеры
    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getComputerNumber() { return computerNumber; }
    public String getSessionTime() { return sessionTime; }
    public String getRegistrationDate() { return registrationDate; }

    // Логика для определения активности с передачей текущего времени
    public boolean isActive(Date currentDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date startDate = sdf.parse(sessionTime);
            long endTimeMillis = startDate.getTime() + 60 * 60 * 1000; // 1 час
            Date endDate = new Date(endTimeMillis);
            // Сессия активна, если текущее время между startDate и endDate, или если startDate в будущем
            return currentDate.after(startDate) && currentDate.before(endDate) || startDate.after(currentDate);
        } catch (Exception e) {
            Log.e("SessionResponse", "Error parsing dates in isActive()", e);
            return false;
        }
    }

    // Оригинальный метод для использования в продакшене
    public boolean isActive() {
        return isActive(new Date());
    }

    // Используем sessionTime как startTime
    public String getStartTime() {
        return sessionTime;
    }

    // Вычисляем endTime как sessionTime + 1 час
    public String getEndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date startDate = sdf.parse(sessionTime);
            long endTimeMillis = startDate.getTime() + 60 * 60 * 1000; // Добавляем 1 час
            return sdf.format(new Date(endTimeMillis));
        } catch (Exception e) {
            Log.e("SessionResponse", "Error calculating endTime", e);
            return sessionTime;
        }
    }

    @Override
    public String toString() {
        return "SessionResponse{client_id=" + id +
                ", full_name='" + fullName + '\'' +
                ", computer_number='" + computerNumber + '\'' +
                ", session_time='" + sessionTime + '\'' +
                ", registration_date='" + registrationDate + "'}";
    }
}