package com.example.tdsclub.models;

public class SessionEntry {
    private int client_id;
    private String full_name;
    private String computer_number;
    private String session_time;
    private String registration_date;

    // Геттеры и сеттеры
    public int getClientId() { return client_id; }
    public void setClientId(int client_id) { this.client_id = client_id; }
    public String getFullName() { return full_name; }
    public void setFullName(String full_name) { this.full_name = full_name; }
    public String getComputerNumber() { return computer_number; }
    public void setComputerNumber(String computer_number) { this.computer_number = computer_number; }
    public String getSessionTime() { return session_time; }
    public void setSessionTime(String session_time) { this.session_time = session_time; }
    public String getRegistrationDate() { return registration_date; }
    public void setRegistrationDate(String registration_date) { this.registration_date = registration_date; }
}