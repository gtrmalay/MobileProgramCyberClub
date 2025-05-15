package com.example.rpmproekt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdsclub.api.RetrofitClient;
import com.example.tdsclub.api.AuthManager;
import com.example.tdsclub.models.SessionResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivityActivity extends AppCompatActivity {

    private static final String TAG = "BookingActivity";
    private RecyclerView recordsRecyclerView;
    private SessionAdapter sessionAdapter;
    private List<SessionResponse> activeSessions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.booking_activity);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Инициализация RecyclerView
        recordsRecyclerView = findViewById(R.id.recordsRecyclerView);
        recordsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sessionAdapter = new SessionAdapter(activeSessions, this::cancelSession);
        recordsRecyclerView.setAdapter(sessionAdapter);

        // Загрузка активных записей
        loadActiveSessions();

        // Настройка нижней навигации
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadActiveSessions(); // Обновляем данные при возвращении на экран
        setupBottomNavigation(); // Убеждаемся, что навигация обновлена
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_booking);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Class<?> targetActivity = null;

            if (id == R.id.nav_menu) {
                targetActivity = MenuActivity1Activity.class;
            } else if (id == R.id.nav_my_club) {
                targetActivity = LkActivityActivity.class;
            } else if (id == R.id.nav_booking) {
                return true; // Останемся на текущем экране
            }

            if (targetActivity != null && !this.getClass().equals(targetActivity)) {
                Intent intent = new Intent(BookingActivityActivity.this, targetActivity);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void loadActiveSessions() {
        String token = AuthManager.getToken(this);
        if (token == null) {
            Toast.makeText(this, "Требуется авторизация", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, NumberEnterActivity.class));
            return;
        }

        Log.d(TAG, "Loading sessions with token: " + token);
        RetrofitClient.getApiService().getSessions("Bearer " + token)
                .enqueue(new Callback<List<SessionResponse>>() {
                    @Override
                    public void onResponse(Call<List<SessionResponse>> call, Response<List<SessionResponse>> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "Raw Response: " + response.body());
                            if (response.body() != null) {
                                activeSessions.clear();
                                for (SessionResponse session : response.body()) {
                                    Log.d(TAG, "Session: " + session.toString() + ", isActive: " + session.isActive());
                                    if (session.isActive()) {
                                        activeSessions.add(session);
                                    }
                                }
                                Log.d(TAG, "Active sessions count: " + activeSessions.size());
                                sessionAdapter.notifyDataSetChanged();
                            } else {
                                Log.e(TAG, "Response body is null");
                                Toast.makeText(BookingActivityActivity.this, "Не удалось загрузить записи", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e(TAG, "Failed to load sessions: " + response.code() + " - " + response.message());
                            Toast.makeText(BookingActivityActivity.this, "Не удалось загрузить записи", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SessionResponse>> call, Throwable t) {
                        Log.e(TAG, "Network error while loading sessions", t);
                        Toast.makeText(BookingActivityActivity.this, "Ошибка сети", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void cancelSession(int sessionId) {
        String token = AuthManager.getToken(this);
        if (token == null) {
            Toast.makeText(this, "Требуется авторизация", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitClient.getApiService().cancelSession("Bearer " + token, sessionId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(BookingActivityActivity.this, "Запись отменена", Toast.LENGTH_SHORT).show();
                            loadActiveSessions(); // Обновляем список после отмены
                        } else {
                            Log.e(TAG, "Failed to cancel session: " + response.code() + " - " + response.message());
                            Toast.makeText(BookingActivityActivity.this, "Ошибка отмены записи", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e(TAG, "Network error while canceling session", t);
                        Toast.makeText(BookingActivityActivity.this, "Ошибка сети", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}