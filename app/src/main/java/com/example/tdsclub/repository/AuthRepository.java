package com.example.tdsclub.repository;

import android.content.Context;
import com.example.tdsclub.api.ApiService;
import com.example.tdsclub.api.AuthManager;
import com.example.tdsclub.models.LoginRequest;
import com.example.tdsclub.models.LoginResponse;
import retrofit2.Call;
import retrofit2.Response;

public class AuthRepository {
    private final ApiService apiService;

    public AuthRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public void login(Context context, String email, String password) {
        try {
            Call<LoginResponse> call = apiService.login(new LoginRequest(email, password));
            Response<LoginResponse> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                AuthManager.saveToken(context, response.body().getToken());
                AuthManager.saveEmail(context, email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}