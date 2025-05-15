package com.example.rpmproekt;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tdsclub.api.RetrofitClient;
import com.example.tdsclub.api.AuthManager;
import com.example.tdsclub.models.SessionResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LkActivityActivity extends AppCompatActivity {

    private static final String TAG = "LkActivity";
    private TextView balanceTextView;
    private TextView bookingStatusText;
    private TextView userNameTextView;
    private TextView userEmailTextView;
    private LinearLayout bookingInfoLayout;
    private double balance = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.lk_activity);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Инициализация элементов UI
        balanceTextView = findViewById(R.id.balanceTextView);
        bookingStatusText = findViewById(R.id.bookingStatusText);
        bookingInfoLayout = findViewById(R.id.bookingInfoLayout);
        userNameTextView = findViewById(R.id.userName);
        userEmailTextView = findViewById(R.id.userEmail);

        // Установка имени и email пользователя
        setUserDetails();

        // Настройка обработчиков
        setupRechargeButton();
        setupBookButton();

        // Настройка нижней навигации
        setupBottomNavigation();

        // Загрузка данных
        loadActiveBookings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadActiveBookings(); // Обновляем данные при возвращении на экран
        setUserDetails(); // Обновляем имя и email, если данные изменились
        setupBottomNavigation(); // Убеждаемся, что навигация обновлена
    }

    private void setUserDetails() {
        String userName = AuthManager.getFio(this);
        String userEmail = AuthManager.getEmail(this);
        Log.d(TAG, "Fetched UserName from AuthManager: " + (userName != null ? userName : "null"));
        Log.d(TAG, "Fetched UserEmail from AuthManager: " + (userEmail != null ? userEmail : "null"));
        if (userName != null && !userName.isEmpty()) {
            userNameTextView.setText(userName);
        } else {
            userNameTextView.setText("Пользователь");
        }
        if (userEmail != null && !userEmail.isEmpty()) {
            userEmailTextView.setText(userEmail);
        } else {
            userEmailTextView.setText("email@example.com");
        }
    }

    private void setupRechargeButton() {
        LinearLayout rechargeButton = findViewById(R.id.rechargeButton);
        rechargeButton.setOnClickListener(v -> showRechargeDialog());
    }

    private void setupBookButton() {
        LinearLayout bookButton = findViewById(R.id.book_button);
        bookButton.setOnClickListener(v -> {
            Intent intent = new Intent(LkActivityActivity.this, BookingActivity4Activity.class);
            startActivity(intent);
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_my_club);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Class<?> targetActivity = null;

            if (id == R.id.nav_menu) {
                targetActivity = MenuActivity1Activity.class;
            } else if (id == R.id.nav_my_club) {
                return true; // Останемся на текущем экране
            } else if (id == R.id.nav_booking) {
                targetActivity = BookingActivityActivity.class;
            }

            if (targetActivity != null && !this.getClass().equals(targetActivity)) {
                Intent intent = new Intent(LkActivityActivity.this, targetActivity);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void loadActiveBookings() {
        String token = AuthManager.getToken(this);
        if (token == null) {
            Toast.makeText(this, "Требуется авторизация", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitClient.getApiService().getSessions("Bearer " + token)
                .enqueue(new Callback<List<SessionResponse>>() {
                    @Override
                    public void onResponse(Call<List<SessionResponse>> call, Response<List<SessionResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            updateBookingUI(filterActiveSessions(response.body()));
                        } else {
                            showNoBookings();
                            Toast.makeText(LkActivityActivity.this, "Не удалось загрузить бронирования", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SessionResponse>> call, Throwable t) {
                        showNoBookings();
                        Log.e(TAG, "Ошибка загрузки бронирований", t);
                    }
                });
    }

    private List<SessionResponse> filterActiveSessions(List<SessionResponse> sessions) {
        List<SessionResponse> activeSessions = new ArrayList<>();
        for (SessionResponse session : sessions) {
            if (session.isActive()) {
                activeSessions.add(session);
            }
        }
        return activeSessions;
    }

    private void updateBookingUI(List<SessionResponse> activeSessions) {
        bookingInfoLayout.removeAllViews();

        if (activeSessions.isEmpty()) {
            showNoBookings();
            return;
        }

        bookingStatusText.setText("Активные бронирования:");

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        for (SessionResponse session : activeSessions) {
            try {
                View bookingView = createBookingView(
                        session.getComputerNumber(),
                        session.getStartTime(),
                        session.getEndTime(),
                        inputFormat,
                        dateFormat,
                        timeFormat
                );
                bookingInfoLayout.addView(bookingView);
            } catch (ParseException e) {
                Log.e(TAG, "Ошибка парсинга даты", e);
            }
        }
    }

    private View createBookingView(String computerNumber, String startTime, String endTime,
                                   SimpleDateFormat inputFormat, SimpleDateFormat dateFormat,
                                   SimpleDateFormat timeFormat) throws ParseException {
        Date startDate = inputFormat.parse(startTime);
        Date endDate = inputFormat.parse(endTime);

        String dateStr = dateFormat.format(startDate);
        String timeStr = timeFormat.format(startDate) + " - " + timeFormat.format(endDate);

        View bookingView = LayoutInflater.from(this).inflate(R.layout.item_booking, bookingInfoLayout, false);

        TextView computerText = bookingView.findViewById(R.id.computerText);
        TextView timeText = bookingView.findViewById(R.id.timeText);

        computerText.setText("Компьютер: " + computerNumber);
        timeText.setText(dateStr + ", " + timeStr);

        return bookingView;
    }

    private void showNoBookings() {
        bookingStatusText.setText("Нет активных бронирований");
        bookingInfoLayout.removeAllViews();
    }

    private void showRechargeDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.popolnenie, null);
        EditText amountEditText = dialogView.findViewById(R.id.amountEditText);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Пополнить баланс")
                .setView(dialogView)
                .setCancelable(true)
                .create();

        confirmButton.setOnClickListener(v -> {
            String amountStr = amountEditText.getText().toString().trim();
            if (amountStr.isEmpty()) {
                Toast.makeText(this, "Введите сумму", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    Toast.makeText(this, "Сумма должна быть положительной", Toast.LENGTH_SHORT).show();
                } else {
                    balance += amount;
                    updateBalanceDisplay();
                    dialog.dismiss();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Некорректное число", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void updateBalanceDisplay() {
        balanceTextView.setText(String.format("%.2f ₽", balance));
    }
}