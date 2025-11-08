package com.example.netbankingapp.ui.transactions;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.netbankingapp.R;
import com.example.netbankingapp.network.ApiClient;
import com.example.netbankingapp.network.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.netbankingapp.R;

public class TransactionHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TransactionAdapter adapter;
    String cifNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction_history);

        recyclerView = findViewById(R.id.recyclerTransactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cifNumber = getIntent().getStringExtra("cif_number");

        fetchTransactions();
    }

    private void fetchTransactions() {
        ApiService.TransactionsRequest req = new ApiService.TransactionsRequest(cifNumber);
        ApiClient.getApiService().getTransactions(req).enqueue(new Callback<ApiService.TransactionsResponse>() {
            @Override
            public void onResponse(Call<ApiService.TransactionsResponse> call, Response<ApiService.TransactionsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<ApiService.Transaction> txns = response.body().getTransactions();
                    adapter = new TransactionAdapter(txns);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(TransactionHistoryActivity.this, "No transactions found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiService.TransactionsResponse> call, Throwable t) {
                Toast.makeText(TransactionHistoryActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}