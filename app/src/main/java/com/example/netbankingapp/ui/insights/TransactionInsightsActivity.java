package com.example.netbankingapp.ui.insights;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.netbankingapp.R;
import com.example.netbankingapp.network.ApiClient;
import com.example.netbankingapp.network.ApiService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionInsightsActivity extends AppCompatActivity {

    private PieChart pieChart;
    private BarChart barChart;
    private TextView tvTotalExpense, tvNoData;
    private String cifNumber;

    // Predefined colors for charts
    private int[] colors = {
            Color.parseColor("#FF6B6B"), // Red
            Color.parseColor("#4ECDC4"), // Teal
            Color.parseColor("#45B7D1"), // Blue
            Color.parseColor("#FFA07A"), // Light Salmon
            Color.parseColor("#98D8C8"), // Mint
            Color.parseColor("#F7DC6F"), // Yellow
            Color.parseColor("#BB8FCE"), // Purple
            Color.parseColor("#85C1E2"), // Sky Blue
            Color.parseColor("#F8B739"), // Orange
            Color.parseColor("#52BE80")  // Green
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction_insights);

        cifNumber = getIntent().getStringExtra("cif_number");
        if (cifNumber == null) {
            Toast.makeText(this, "CIF number not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupCharts();
        fetchInsights();
    }

    private void initViews() {
        pieChart = findViewById(R.id.pieChart);
        barChart = findViewById(R.id.barChart);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        tvNoData = findViewById(R.id.tvNoData);
    }

    private void setupCharts() {
        // Setup Pie Chart
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setCenterText("Expenses\nby Category");
        pieChart.setCenterTextSize(14f);
        pieChart.getLegend().setEnabled(true);
        pieChart.getLegend().setTextSize(12f);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setEntryLabelColor(Color.BLACK);

        // Setup Bar Chart
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.getLegend().setEnabled(true);
        barChart.getLegend().setTextSize(12f);
        
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(-45f);
        xAxis.setTextSize(10f);
        
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextSize(10f);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "₹" + String.format("%.0f", value);
            }
        });
        
        barChart.getAxisRight().setEnabled(false);
    }

    private void fetchInsights() {
        ApiService.InsightsRequest request = new ApiService.InsightsRequest(cifNumber);
        ApiClient.getApiService().getTransactionInsights(request).enqueue(new Callback<ApiService.InsightsResponse>() {
            @Override
            public void onResponse(Call<ApiService.InsightsResponse> call, Response<ApiService.InsightsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    ApiService.InsightsResponse insightsResponse = response.body();
                    List<ApiService.CategoryInsight> insights = insightsResponse.getInsights();
                    List<ApiService.MonthlyTrend> monthlyTrend = insightsResponse.getMonthly_trend();

                    if (insights != null && !insights.isEmpty()) {
                        tvNoData.setVisibility(android.view.View.GONE);
                        setupPieChart(insights);
                        if (monthlyTrend != null && !monthlyTrend.isEmpty()) {
                            setupBarChart(monthlyTrend);
                        }
                        tvTotalExpense.setText("Total Expenses: ₹" + String.format("%.2f", insightsResponse.getTotal_expense()));
                    } else {
                        showNoData();
                    }
                } else {
                    Toast.makeText(TransactionInsightsActivity.this, "Failed to load insights", Toast.LENGTH_SHORT).show();
                    showNoData();
                }
            }

            @Override
            public void onFailure(Call<ApiService.InsightsResponse> call, Throwable t) {
                Toast.makeText(TransactionInsightsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                showNoData();
            }
        });
    }

    private void showNoData() {
        tvNoData.setVisibility(android.view.View.VISIBLE);
        pieChart.setVisibility(android.view.View.GONE);
        barChart.setVisibility(android.view.View.GONE);
    }

    private void setupPieChart(List<ApiService.CategoryInsight> insights) {
        List<PieEntry> entries = new ArrayList<>();
        
        for (ApiService.CategoryInsight insight : insights) {
            entries.add(new PieEntry((float) insight.total_amount, insight.category));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "₹" + String.format("%.0f", value);
            }
        });

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.setVisibility(android.view.View.VISIBLE);
    }

    private void setupBarChart(List<ApiService.MonthlyTrend> monthlyTrend) {
        if (monthlyTrend == null || monthlyTrend.isEmpty()) {
            barChart.setVisibility(android.view.View.GONE);
            return;
        }

        List<BarEntry> entries = new ArrayList<>();
        final List<String> labels = new ArrayList<>();

        for (int i = 0; i < monthlyTrend.size(); i++) {
            ApiService.MonthlyTrend trend = monthlyTrend.get(i);
            entries.add(new BarEntry(i, (float) trend.total));
            labels.add(trend.month);
        }

        BarDataSet dataSet = new BarDataSet(entries, "Monthly Expenses");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0) return "";
                return "₹" + String.format("%.0f", value);
            }
        });

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.7f);
        barChart.setData(barData);

        // Set X-axis labels
        barChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < labels.size()) {
                    return labels.get(index);
                }
                return "";
            }
        });

        barChart.invalidate();
        barChart.setVisibility(android.view.View.VISIBLE);
    }
}

