package com.example.rpmproekt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tdsclub.api.AuthManager;
import com.example.tdsclub.api.RetrofitClient;
import com.example.tdsclub.models.RegisterRequest;
import com.example.tdsclub.models.RegisterResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegEndActivity extends AppCompatActivity {
    private TextInputEditText nameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText confirmPasswordEditText;
    private MaterialButton continueButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_end);

        // Инициализация элементов
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        continueButton = findViewById(R.id.continueButton);
        progressBar = findViewById(R.id.progressBar);

        // Обработчик кнопки "Продолжить"
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                // Валидация полей
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegEndActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegEndActivity.this, "Неверный формат email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(RegEndActivity.this, "Пароль должен быть не менее 6 символов", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegEndActivity.this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                continueButton.setEnabled(false);

                RegisterRequest request = new RegisterRequest(email, password, name);
                RetrofitClient.getApiService().register(request).enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                        progressBar.setVisibility(View.GONE);
                        continueButton.setEnabled(true);

                        if (response.isSuccessful() && response.body() != null) {
                            AuthManager.saveToken(RegEndActivity.this, response.body().getToken());
                            Toast.makeText(RegEndActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();

                            String userName = nameEditText.getText().toString().trim();
                            String userEmail = emailEditText.getText().toString().trim();

                            AuthManager.saveFio(RegEndActivity.this, userName);
                            AuthManager.saveEmail(RegEndActivity.this, userEmail);

                            Toast.makeText(RegEndActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();

                            // Переход на личный кабинет
                            Intent intent = new Intent(RegEndActivity.this, LkActivityActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        continueButton.setEnabled(true);
                        Toast.makeText(RegEndActivity.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // Обработчик кнопки "Назад"
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}