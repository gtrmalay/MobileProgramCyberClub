package com.example.rpmproekt;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tdsclub.api.RetrofitClient;
import com.example.tdsclub.api.AuthManager;
import com.example.tdsclub.models.Computer;
import com.example.tdsclub.models.CreateSessionRequest;
import com.example.tdsclub.models.SessionEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivity4Activity extends AppCompatActivity {

    private static final String TAG = "BookingActivity";
    private TextView dateSelected, timeSelected, locationSelected;
    private MaterialButton loginButton;
    private ProgressBar progressBar;

    private final Map<String, Integer> computerNumberToId = new HashMap<>();
    private String selectedComputerNumber = ""; // Ключевое изменение 1: Хранение выбранного номера

    // Форматы даты/времени
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private final SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_activity4);

        initViews();
        loadComputers();
        setupListeners();
        setupNavigation();
    }

    private void initViews() {
        dateSelected = findViewById(R.id.date_selected);
        timeSelected = findViewById(R.id.time_selected);
        locationSelected = findViewById(R.id.location_selected);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        findViewById(R.id.date_card).setOnClickListener(v -> showDatePicker());
        findViewById(R.id.time_card).setOnClickListener(v -> showTimePicker());
        findViewById(R.id.location_card).setOnClickListener(v -> showLocationPicker());

        loginButton.setOnClickListener(v -> attemptBooking());
    }

    private void attemptBooking() {
        try {
            // Разбиваем время на части
            String[] timeParts = timeSelected.getText().toString().split(" - "); // Исправлено использование split()
            if (timeParts.length < 1) {
                Toast.makeText(this, "Некорректный формат времени", Toast.LENGTH_SHORT).show();
                return;
            }

            // Формирование времени
            String dateTimeStr = dateSelected.getText() + " " + timeParts[0];
            SimpleDateFormat parser = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            Date date = parser.parse(dateTimeStr);
            String isoTime = isoFormat.format(date);

            // Создание запроса
            CreateSessionRequest request = new CreateSessionRequest(
                    selectedComputerNumber,
                    isoTime,
                    1
            );

            // Логирование с использованием Gson
            String jsonRequest = new Gson().toJson(request); // Правильное использование Gson
            Log.d(TAG, "Отправка запроса: " + jsonRequest);

            sendBookingRequest(request);
        } catch (Exception e) {
            Log.e(TAG, "Ошибка формирования запроса", e);
            Toast.makeText(this, "Ошибка создания брони", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendBookingRequest(CreateSessionRequest request) {
        String token = AuthManager.getToken(this);
        if (token == null) {
            Toast.makeText(this, "Требуется авторизация", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, NumberEnterActivity.class));
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        RetrofitClient.getApiService().createSession("Bearer " + token, request)
                .enqueue(new Callback<SessionEntry>() {
                    @Override
                    public void onResponse(Call<SessionEntry> call, Response<SessionEntry> response) {
                        progressBar.setVisibility(View.GONE);
                        loginButton.setEnabled(true);

                        if (response.isSuccessful()) {
                            Toast.makeText(BookingActivity4Activity.this,
                                    "Бронирование успешно!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            String error = "Ошибка: " + response.code();
                            try {
                                if (response.errorBody() != null) {
                                    error = response.errorBody().string();
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing error", e);
                            }
                            Toast.makeText(BookingActivity4Activity.this, error, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SessionEntry> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                        Toast.makeText(BookingActivity4Activity.this,
                                "Ошибка сети: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Network error", t);
                    }
                });
    }

    private void loadComputers() {
        RetrofitClient.getApiService().getComputers().enqueue(new Callback<List<Computer>>() {
            @Override
            public void onResponse(Call<List<Computer>> call, Response<List<Computer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    computerNumberToId.clear();
                    for (Computer computer : response.body()) {
                        if (computer.isFree()) {
                            computerNumberToId.put(computer.getComputerNumber(), computer.getComputerId());
                        }
                    }
                    Log.d(TAG, "Загружено компьютеров: " + computerNumberToId.size());
                } else {
                    Log.e(TAG, "Ошибка загрузки компьютеров: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Computer>> call, Throwable t) {
                Log.e(TAG, "Ошибка сети при загрузке компьютеров", t);
            }
        });
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            cal.set(year, month, day);
            dateSelected.setText(dateFormat.format(cal.getTime()));
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        Calendar cal = Calendar.getInstance();
        new TimePickerDialog(this, (view, hour, minute) -> {
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, minute);
            // Форматируем время как "HH:mm - HH:mm" (фиксированная длительность 1 час)
            String endTime = String.format(Locale.getDefault(), "%02d:%02d", (hour + 1) % 24, minute);
            timeSelected.setText(timeFormat.format(cal.getTime()) + " - " + endTime);
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
    }

    private void showLocationPicker() {
        String[] computers = computerNumberToId.keySet()
                .stream()
                .sorted()
                .toArray(String[]::new);

        new AlertDialog.Builder(this)
                .setTitle("Выберите компьютер (доступно: " + computers.length + ")")
                .setItems(computers, (dialog, which) -> {
                    selectedComputerNumber = computers[which];
                    locationSelected.setText(selectedComputerNumber);

                    // Логирование выбора
                    Log.d(TAG, "Выбрано: " + selectedComputerNumber +
                            " (ID: " + computerNumberToId.get(selectedComputerNumber) + ")");
                })
                .show();
    }

    private void setupNavigation() {
        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        navView.setOnItemSelectedListener(item -> {
            Intent intent = null;
            int id = item.getItemId();

            if (id == R.id.nav_my_club) {
                intent = new Intent(this, LkActivityActivity.class);
            } else if (id == R.id.nav_menu) {
                intent = new Intent(this, MenuActivity1Activity.class);
            } else if (id == R.id.nav_booking) {
                return true; // Уже на этом экране
            }

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            }

            return false;
        });

        // Обновление подсветки в onResume() после возвращения на активность
        navView.setSelectedItemId(R.id.nav_booking); // Устанавливаем выбранный элемент по умолчанию
    }
}