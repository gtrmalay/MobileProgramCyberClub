package com.example.rpmproekt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация кнопки для перехода в NumberEnterActivity
        MaterialButton goToLoginButton = findViewById(R.id.goToLoginButton);

        goToLoginButton.setOnClickListener(v -> {
            // Переход на экран с вводом данных
            Intent intent = new Intent(MainActivity.this, NumberEnterActivity.class);
            startActivity(intent);
        });
    }
}
