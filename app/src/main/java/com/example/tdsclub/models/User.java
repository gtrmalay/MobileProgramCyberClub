package com.example.tdsclub.models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("fullName")
    private String fullName;

    @SerializedName("name")
    private String name;

    @SerializedName("fio")
    private String fio;

    @SerializedName("email")
    private String email;

    public User() {
        // Пустой конструктор
    }

    // Геттер для полного имени — возвращает первое непустое из fullName, name, fio
    public String getFullName() {
        if (fullName != null && !fullName.isEmpty()) {
            return fullName;
        } else if (name != null && !name.isEmpty()) {
            return name;
        } else if (fio != null && !fio.isEmpty()) {
            return fio;
        }
        return null;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{fullName='" + fullName + "', name='" + name + "', fio='" + fio + "', email='" + email + "'}";
    }
}
