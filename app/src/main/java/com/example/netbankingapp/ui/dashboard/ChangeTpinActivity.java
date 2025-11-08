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

public class ChangeTpinActivity extends AppCompatActivity {

    EditText etOldTpin, etNewTpin;
    Button btnChangeTpin;
    String cifNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_tpin);

        etOldTpin = findViewById(R.id.etOldTpin);
        etNewTpin = findViewById(R.id.etNewTpin);
        btnChangeTpin = findViewById(R.id.btnChangeTpin);

        cifNumber = getIntent().getStringExtra("cif_number");

        btnChangeTpin.setOnClickListener(v -> {
            String oldPin = etOldTpin.getText().toString().trim();
            String newPin = etNewTpin.getText().toString().trim();

            if (oldPin.isEmpty() || newPin.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService.ChangePinRequest req = new ApiService.ChangePinRequest(cifNumber, oldPin, newPin);
            ApiClient.getApiService().changeTpin(req).enqueue(new Callback<ApiService.SimpleResponse>() {
                @Override
                public void onResponse(Call<ApiService.SimpleResponse> call, Response<ApiService.SimpleResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(ChangeTpinActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (response.body().isSuccess()) finish();
                    }
                }

                @Override
                public void onFailure(Call<ApiService.SimpleResponse> call, Throwable t) {
                    Toast.makeText(ChangeTpinActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}