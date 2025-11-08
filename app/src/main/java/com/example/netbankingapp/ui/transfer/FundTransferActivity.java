package com.example.netbankingapp.ui.transfer;

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

public class FundTransferActivity extends AppCompatActivity {

    private EditText etReceiverCif, etAmount, etTransactionPin;
    private Button btnTransfer;
    private String senderCif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fund_transfer);

        etReceiverCif = findViewById(R.id.etReceiverCif);
        etAmount = findViewById(R.id.etAmount);
        etTransactionPin = findViewById(R.id.etTransactionPin);
        btnTransfer = findViewById(R.id.btnTransfer);

        senderCif = getIntent().getStringExtra("cif_number");
        if (senderCif == null) {
            Toast.makeText(this, "CIF number not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnTransfer.setOnClickListener(v -> performTransfer());
    }

    private void performTransfer() {
        String toCif = etReceiverCif.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String tpin = etTransactionPin.getText().toString().trim();

        if (toCif.isEmpty() || amountStr.isEmpty() || tpin.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (toCif.equals(senderCif)) {
            Toast.makeText(this, "Cannot transfer to your own account", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                Toast.makeText(this, "Amount must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable button to prevent multiple clicks
        btnTransfer.setEnabled(false);
        btnTransfer.setText("Processing...");

        ApiService.TransferRequest req = new ApiService.TransferRequest(senderCif, toCif, amount, tpin);

        ApiClient.getApiService().transfer(req).enqueue(new Callback<ApiService.TransferResponse>() {
            @Override
            public void onResponse(Call<ApiService.TransferResponse> call, Response<ApiService.TransferResponse> response) {
                btnTransfer.setEnabled(true);
                btnTransfer.setText("Transfer Funds");

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(FundTransferActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if (response.body().isSuccess()) {
                        finish(); // Return to dashboard
                    }
                } else {
                    Toast.makeText(FundTransferActivity.this, "Transfer failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiService.TransferResponse> call, Throwable t) {
                btnTransfer.setEnabled(true);
                btnTransfer.setText("Transfer Funds");
                Toast.makeText(FundTransferActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}