package com.example.netbankingapp.ui.register;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netbankingapp.R;
import com.example.netbankingapp.network.ApiClient;
import com.example.netbankingapp.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText etAccountNumber, etCifNumber, etBranchCode, etMobile, etPassword, etTransactionPin;
    Spinner spinnerCountry;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etAccountNumber = findViewById(R.id.etAccountNumber);
        etCifNumber = findViewById(R.id.etCifNumber);
        etBranchCode = findViewById(R.id.etBranchCode);
        spinnerCountry = findViewById(R.id.spinnerCountry);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        etTransactionPin = findViewById(R.id.etTransactionPin);
        btnRegister = findViewById(R.id.btnRegister);

        // Country Spinner setup
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.countries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(adapter);

        btnRegister.setOnClickListener(v -> {
            String acc = etAccountNumber.getText().toString().trim();
            String cif = etCifNumber.getText().toString().trim();
            String branch = etBranchCode.getText().toString().trim();
            String country = spinnerCountry.getSelectedItem().toString();
            String mobile = etMobile.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();
            String transactionPin = etTransactionPin.getText().toString().trim();

            // ✅ Simple validations
            if (acc.length() != 16) {
                etAccountNumber.setError("16 digits required");
                return;
            }
            if (cif.length() != 11) {
                etCifNumber.setError("11 digits required");
                return;
            }
            if (branch.length() != 5) {
                etBranchCode.setError("5 digits required");
                return;
            }
            if (mobile.isEmpty()) {
                etMobile.setError("Enter mobile number");
                return;
            }

            // Extra rule: If country is India → Mobile must be 10 digits
            if (country.equalsIgnoreCase("India") && mobile.length() != 10) {
                etMobile.setError("Mobile number must be 10 digits for India");
                return;
            }
            if (transactionPin.length() != 4) {
                etTransactionPin.setError("4-digit TPIN required");
                return;
            }

            // ✅ API request
            ApiService.RegisterRequest req = new ApiService.RegisterRequest(
                    acc, cif, branch, country, mobile, pass, transactionPin);

            ApiClient.getApiService().register(req).enqueue(new Callback<ApiService.LoginResponse>() {
                @Override
                public void onResponse(Call<ApiService.LoginResponse> call, Response<ApiService.LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (response.body().isSuccess()) {
                            finish(); // Go back to login
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiService.LoginResponse> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
