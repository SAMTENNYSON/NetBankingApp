package com.example.netbankingapp.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netbankingapp.R;
import com.example.netbankingapp.network.ApiClient;
import com.example.netbankingapp.network.ApiService;
import com.example.netbankingapp.ui.dashboard.DashboardActivity;
import com.example.netbankingapp.ui.register.RegisterActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etCifNumber, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etCifNumber = findViewById(R.id.etCifNumber);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String cif = etCifNumber.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (cif.isEmpty()) {
                etCifNumber.setError("Enter CIF number");
                return;
            }
            if (pass.isEmpty()) {
                etPassword.setError("Enter password");
                return;
            }

            ApiService.LoginRequest req = new ApiService.LoginRequest(cif, pass);
            ApiClient.getApiService().login(req).enqueue(new Callback<ApiService.LoginResponse>() {
                @Override
                public void onResponse(Call<ApiService.LoginResponse> call, Response<ApiService.LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (response.body().isSuccess()) {
                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            intent.putExtra("cif_number", cif);
                            startActivity(intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiService.LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        Button btnGoRegister = findViewById(R.id.btnGoRegister);
        btnGoRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}