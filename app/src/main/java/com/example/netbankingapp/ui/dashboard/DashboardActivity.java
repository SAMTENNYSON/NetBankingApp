package com.example.netbankingapp.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netbankingapp.R;
import com.example.netbankingapp.network.ApiClient;
import com.example.netbankingapp.network.ApiService;
import com.example.netbankingapp.ui.calculator.EMICalculatorActivity;
import com.example.netbankingapp.ui.insights.TransactionInsightsActivity;
import com.example.netbankingapp.ui.profile.ProfileActivity;
import com.example.netbankingapp.ui.qrcode.QRCodePaymentActivity;
import com.example.netbankingapp.ui.transactions.TransactionHistoryActivity;
import com.example.netbankingapp.ui.transfer.FundTransferActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    TextView tvBalance;
    String cifNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        cifNumber = getIntent().getStringExtra("cif_number");
        tvBalance = findViewById(R.id.tvBalance);

        fetchBalance();

        // ✅ ImageView for Transactions
        ImageView ivTransactions = findViewById(R.id.ivTransactions);
        ivTransactions.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, TransactionHistoryActivity.class);
            intent.putExtra("cif_number", cifNumber);
            startActivity(intent);
        });

        // ✅ ImageView for Fund Transfer
        ImageView ivTransfer = findViewById(R.id.ivTransfer);
        ivTransfer.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, FundTransferActivity.class);
            intent.putExtra("cif_number", cifNumber);
            startActivity(intent);
        });

        // ✅ ImageView for QR Code Payment
        ImageView ivQRPayment = findViewById(R.id.ivQRPayment);
        ivQRPayment.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, QRCodePaymentActivity.class);
            intent.putExtra("cif_number", cifNumber);
            startActivity(intent);
        });

        // ✅ ImageView for Transaction Insights
        ImageView ivInsights = findViewById(R.id.ivInsights);
        ivInsights.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, TransactionInsightsActivity.class);
            intent.putExtra("cif_number", cifNumber);
            startActivity(intent);
        });

        // ✅ ImageView for EMI Calculator
        ImageView ivEMICalculator = findViewById(R.id.ivEMICalculator);
        ivEMICalculator.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, EMICalculatorActivity.class);
            startActivity(intent);
        });

        // Change Password button
        Button btnChangePassword = findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ChangePasswordActivity.class);
            intent.putExtra("cif_number", cifNumber);
            startActivity(intent);
        });

        // Change TPIN button
        Button btnChangeTpin = findViewById(R.id.btnChangeTpin);
        btnChangeTpin.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ChangeTpinActivity.class);
            intent.putExtra("cif_number", cifNumber);
            startActivity(intent);
        });

        // Profile icon
        ImageView ivProfile = findViewById(R.id.ivProfile);
        ivProfile.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            intent.putExtra("cif_number", cifNumber);
            // (Optional: replace hardcoded values with real data from API if available)
            intent.putExtra("account_number", "1234567890123456");
            intent.putExtra("branch_code", "54321");
            intent.putExtra("country", "India");
            intent.putExtra("mobile", "9876543210");
            startActivity(intent);
        });

    }

    // Refresh balance when returning to Dashboard
    @Override
    protected void onResume() {
        super.onResume();
        fetchBalance();
    }

    private void fetchBalance() {
        ApiService.BalanceRequest req = new ApiService.BalanceRequest(cifNumber);
        ApiClient.getApiService().getBalance(req).enqueue(new Callback<ApiService.BalanceResponse>() {
            @Override
            public void onResponse(Call<ApiService.BalanceResponse> call, Response<ApiService.BalanceResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    tvBalance.setText("Balance: ₹" + response.body().getBalance());
                } else {
                    Toast.makeText(DashboardActivity.this, "Failed to load balance", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiService.BalanceResponse> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
