package com.example.tdsclub.api;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthManager {

    private static final String TOKEN_KEY = "auth_token";
    private static final String FIO_KEY = "user_fio";
    private static final String PREFS_NAME = "auth_prefs";

    private static final String EMAIL_KEY = "user_email";

    // Сохранение токена в SharedPreferences
    public static void saveToken(Context context, String token) {
        if (token == null || token.isEmpty()) {
            // В случае пустого токена, не сохраняем его
            return;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply(); // Применяем изменения асинхронно
    }

    // Получение токена из SharedPreferences
    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TOKEN_KEY, null);  // Возвращаем null, если токен не найден
    }

    // Очистка токена
    public static void clearToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(TOKEN_KEY);  // Удаляем только токен
        editor.apply();
    }

    // Сохранение данных авторизации (токен и FIO)
    public static void saveAuth(Context context, String token, String fio, String phone, String email) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN_KEY, token);
        editor.putString(FIO_KEY, fio);
        editor.putString(EMAIL_KEY, email);
        editor.apply();
    }
    public static String getEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(EMAIL_KEY, null);
    }

    public static void saveEmail(Context context, String email) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(EMAIL_KEY, email).apply();
    }

    // Получение FIO пользователя
    public static void saveFio(Context context, String fio) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(FIO_KEY, fio).apply();
    }

    public static String getFio(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(FIO_KEY, null);
    }

    // Очистка всех данных авторизации
    public static void clearAuth(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();  // Очищаем все данные
        editor.apply();
    }
}
