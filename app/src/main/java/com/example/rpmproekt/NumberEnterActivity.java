package com.example.rpmproekt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tdsclub.api.AuthManager;
import com.example.tdsclub.api.RetrofitClient;
import com.example.tdsclub.models.LoginRequest;
import com.example.tdsclub.models.LoginResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NumberEnterActivity extends AppCompatActivity {
    private static final String TAG = "NumberEnterActivity";
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private MaterialButton loginButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_enter);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);

        TextView registerText = findViewById(R.id.registerText);
        registerText.setOnClickListener(v -> {
            Intent intent = new Intent(NumberEnterActivity.this, RegEndActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(NumberEnterActivity.this, "Введите email и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(NumberEnterActivity.this, "Неверный формат email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(NumberEnterActivity.this, "Пароль должен быть не менее 6 символов", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            loginButton.setEnabled(false);

            LoginRequest request = new LoginRequest(email, password);
            RetrofitClient.getApiService().login(request).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    loginButton.setEnabled(true);

                    if (response.isSuccessful() && response.body() != null) {
                        AuthManager.saveToken(NumberEnterActivity.this, response.body().getToken());
                        // Логируем полный ответ API
                        Log.d(TAG, "LoginResponse: " + response.body().toString());
                        // Извлекаем имя пользователя с проверкой на null
                        String fullName = null;
                        if (response.body().getUser() != null) {
                            fullName = response.body().getUser().getFullName();
                            Log.d(TAG, "Extracted FullName: " + (fullName != null ? fullName : "null"));
                        } else {
                            Log.d(TAG, "User object is null");
                        }
                        if (fullName != null && !fullName.isEmpty()) {
                            AuthManager.saveFio(NumberEnterActivity.this, fullName);
                            Log.d(TAG, "Saved FullName to AuthManager: " + fullName);
                        } else {
                            AuthManager.saveFio(NumberEnterActivity.this, "Пользователь");
                            Log.d(TAG, "Saved default name to AuthManager: Пользователь");
                        }
                        AuthManager.saveEmail(NumberEnterActivity.this, email);
                        Toast.makeText(NumberEnterActivity.this, "Вход успешен", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NumberEnterActivity.this, LkActivityActivity.class));
                        finish();
                    } else {
                        Toast.makeText(NumberEnterActivity.this, "Ошибка входа: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    loginButton.setEnabled(true);
                    Toast.makeText(NumberEnterActivity.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        findViewById(R.id.backButton).setOnClickListener(v -> {
            Intent intent = new Intent(NumberEnterActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}