package com.example.netbankingapp.ui.dashboard;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.netbankingapp.R;
import com.example.netbankingapp.network.ApiClient;
import com.example.netbankingapp.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText etOldPassword, etNewPassword;
    Button btnChangePassword;
    String cifNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        cifNumber = getIntent().getStringExtra("cif_number");

        btnChangePassword.setOnClickListener(v -> {
            String oldPass = etOldPassword.getText().toString().trim();
            String newPass = etNewPassword.getText().toString().trim();

            if (oldPass.isEmpty() || newPass.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService.ChangePasswordRequest req = new ApiService.ChangePasswordRequest(cifNumber, oldPass, newPass);
            ApiClient.getApiService().changePassword(req).enqueue(new Callback<ApiService.SimpleResponse>() {
                @Override
                public void onResponse(Call<ApiService.SimpleResponse> call, Response<ApiService.SimpleResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(ChangePasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (response.body().isSuccess()) finish();
                    }
                }

                @Override
                public void onFailure(Call<ApiService.SimpleResponse> call, Throwable t) {
                    Toast.makeText(ChangePasswordActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}