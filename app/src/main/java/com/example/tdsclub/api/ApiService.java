package com.example.tdsclub.api;

import com.example.tdsclub.models.Computer;
import com.example.tdsclub.models.SessionEntry;
import com.example.tdsclub.models.CreateSessionRequest;
import com.example.tdsclub.models.CancelSessionResponse;
import com.example.tdsclub.models.RegisterRequest;
import com.example.tdsclub.models.RegisterResponse;
import com.example.tdsclub.models.LoginRequest;
import com.example.tdsclub.models.LoginResponse;
import com.example.tdsclub.models.SessionResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("api/computers")
    Call<List<Computer>> getComputers();

    @POST("api/sessions")
    Call<SessionEntry> createSession(@Header("Authorization") String token, @Body CreateSessionRequest request);

    @GET("api/sessions")
    Call<List<SessionResponse>> getSessions(@Header("Authorization") String token);

    @DELETE("api/sessions/{id}")
    Call<Void> cancelSession(@Header("Authorization") String token, @Path("id") int id);


}