package com.example.netbankingapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netbankingapp.R;
import com.example.netbankingapp.network.ApiClient;
import com.example.netbankingapp.network.ApiService;
import com.example.netbankingapp.ui.login.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    TextView tvCifNumber, tvAccountNumber, tvBranchCode, tvCountry, tvMobile;
    Button btnLogout;
    String cifNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvCifNumber = findViewById(R.id.tvCifNumber);
        tvAccountNumber = findViewById(R.id.tvAccountNumber);
        tvBranchCode = findViewById(R.id.tvBranchCode);
        tvCountry = findViewById(R.id.tvCountry);
        tvMobile = findViewById(R.id.tvMobile);
        btnLogout = findViewById(R.id.btnLogout);

        // Get CIF number from Dashboard
        cifNumber = getIntent().getStringExtra("cif_number");

        fetchProfile();

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void fetchProfile() {
        ApiService.ProfileRequest req = new ApiService.ProfileRequest(cifNumber);
        ApiClient.getApiService().getProfile(req).enqueue(new Callback<ApiService.ProfileResponse>() {
            @Override
            public void onResponse(Call<ApiService.ProfileResponse> call, Response<ApiService.ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    tvCifNumber.setText("CIF Number: " + response.body().getCif_number());
                    tvAccountNumber.setText("Account Number: " + response.body().getAccount_number());
                    tvBranchCode.setText("Branch Code: " + response.body().getBranch_code());
                    tvCountry.setText("Country: " + response.body().getCountry());
                    tvMobile.setText("Mobile: " + response.body().getMobile());
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiService.ProfileResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}