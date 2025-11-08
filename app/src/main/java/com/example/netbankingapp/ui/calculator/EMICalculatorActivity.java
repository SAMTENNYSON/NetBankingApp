package com.example.netbankingapp.ui.calculator;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.netbankingapp.R;
import com.google.android.material.card.MaterialCardView;

public class EMICalculatorActivity extends AppCompatActivity {

    private EditText etPrincipal, etInterestRate, etTenure;
    private Button btnCalculate, btnReset;
    private TextView tvEMIResult, tvTotalAmount, tvTotalInterest;
    private MaterialCardView cardResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_emi_calculator);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etPrincipal = findViewById(R.id.etPrincipal);
        etInterestRate = findViewById(R.id.etInterestRate);
        etTenure = findViewById(R.id.etTenure);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);
        tvEMIResult = findViewById(R.id.tvEMIResult);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvTotalInterest = findViewById(R.id.tvTotalInterest);
        cardResult = findViewById(R.id.cardResult);

        // Initially hide result card
        cardResult.setVisibility(View.GONE);
    }

    private void setupListeners() {
        btnCalculate.setOnClickListener(v -> calculateEMI());
        btnReset.setOnClickListener(v -> resetForm());
    }

    private void calculateEMI() {
        // Get input values
        String principalStr = etPrincipal.getText().toString().trim();
        String interestStr = etInterestRate.getText().toString().trim();
        String tenureStr = etTenure.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(principalStr)) {
            etPrincipal.setError("Enter loan amount");
            etPrincipal.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(interestStr)) {
            etInterestRate.setError("Enter interest rate");
            etInterestRate.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(tenureStr)) {
            etTenure.setError("Enter loan tenure");
            etTenure.requestFocus();
            return;
        }

        try {
            double principal = Double.parseDouble(principalStr);
            double annualInterestRate = Double.parseDouble(interestStr);
            int tenureMonths = Integer.parseInt(tenureStr);

            // Validate positive values
            if (principal <= 0) {
                etPrincipal.setError("Loan amount must be greater than 0");
                etPrincipal.requestFocus();
                return;
            }

            if (annualInterestRate < 0 || annualInterestRate > 100) {
                etInterestRate.setError("Interest rate must be between 0 and 100");
                etInterestRate.requestFocus();
                return;
            }

            if (tenureMonths <= 0) {
                etTenure.setError("Tenure must be greater than 0");
                etTenure.requestFocus();
                return;
            }

            // Calculate EMI
            double emi = calculateEMI(principal, annualInterestRate, tenureMonths);
            double totalAmount = emi * tenureMonths;
            double totalInterest = totalAmount - principal;

            // Display results
            displayResults(emi, totalAmount, totalInterest);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error calculating EMI: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Calculate EMI using the formula:
     * EMI = [P × R × (1+R)^N] / [(1+R)^N – 1]
     * Where:
     * P = Principal amount
     * R = Monthly interest rate (annual rate / 12 / 100)
     * N = Number of months
     */
    private double calculateEMI(double principal, double annualInterestRate, int tenureMonths) {
        // Convert annual interest rate to monthly rate
        double monthlyInterestRate = (annualInterestRate / 12.0) / 100.0;

        // Calculate (1+R)^N
        double powerFactor = Math.pow(1 + monthlyInterestRate, tenureMonths);

        // Calculate EMI
        double emi = (principal * monthlyInterestRate * powerFactor) / (powerFactor - 1);

        // Round to 2 decimal places
        return Math.round(emi * 100.0) / 100.0;
    }

    private void displayResults(double emi, double totalAmount, double totalInterest) {
        tvEMIResult.setText("₹" + String.format("%.2f", emi));
        tvTotalAmount.setText("₹" + String.format("%.2f", totalAmount));
        tvTotalInterest.setText("₹" + String.format("%.2f", totalInterest));

        cardResult.setVisibility(View.VISIBLE);
    }

    private void resetForm() {
        etPrincipal.setText("");
        etInterestRate.setText("");
        etTenure.setText("");
        cardResult.setVisibility(View.GONE);
        
        // Clear errors
        etPrincipal.setError(null);
        etInterestRate.setError(null);
        etTenure.setError(null);
    }
}

