package com.example.rpmproekt;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tdsclub.api.AuthManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.View;
import android.widget.LinearLayout;

public class MenuActivity1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.menu_activity1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Настройка нижней навигации
        setupBottomNavigation();

        // Обработка клика на "Политику конфиденциальности"
        LinearLayout privacyPolicyLayout = findViewById(R.id.privacyPolicyLayout);
        privacyPolicyLayout.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity1Activity.this, politika_activity.class);
            startActivity(intent);
        });

        // Обработка клика на "Пользовательское соглашение"
        LinearLayout sogl = findViewById(R.id.sogl);
        sogl.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity1Activity.this, soglashenie.class);
            startActivity(intent);
        });

        // Обработка клика на "Обратную связь"
        LinearLayout feedbackLayout = findViewById(R.id.feedback);
        feedbackLayout.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity1Activity.this, feedback.class);
            startActivity(intent);
        });

        // Обработка клика на "Выход"
        LinearLayout logoutLayout = findViewById(R.id.logoutLayout);
        logoutLayout.setOnClickListener(v -> {
            AuthManager.clearToken(this);
            Intent intent = new Intent(MenuActivity1Activity.this, NumberEnterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBottomNavigation(); // Убеждаемся, что навигация обновлена
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_menu);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Class<?> targetActivity = null;

            if (id == R.id.nav_menu) {
                return true; // Останемся на текущем экране
            } else if (id == R.id.nav_my_club) {
                targetActivity = LkActivityActivity.class;
            } else if (id == R.id.nav_booking) {
                targetActivity = BookingActivityActivity.class;
            }

            if (targetActivity != null && !this.getClass().equals(targetActivity)) {
                Intent intent = new Intent(MenuActivity1Activity.this, targetActivity);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}