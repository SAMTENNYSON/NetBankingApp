package com.example.netbankingapp.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    class LoginRequest {
        String cif_number;
        String password;

        public LoginRequest(String cif_number, String password) {
            this.cif_number = cif_number;
            this.password = password;
        }
    }

    class LoginResponse {
        boolean success;
        String message;

        public boolean isSuccess() {
            return success;
        }
        public String getMessage() {
            return message;
        }
    }

    @POST("netbankingapi/login.php")
    Call<LoginResponse> login(@Body LoginRequest request);


    class BalanceRequest {
        String cif_number;
        public BalanceRequest(String cif_number) {
            this.cif_number = cif_number;
        }
    }

    class BalanceResponse {
        boolean success;
        double balance;

        public boolean isSuccess() { return success; }
        public double getBalance() { return balance; }
    }

    @POST("netbankingapi/get_balance.php")
    Call<BalanceResponse> getBalance(@Body BalanceRequest request);

    class RegisterRequest {
        String account_number, cif_number, branch_code, country, mobile_number, password, transaction_pin;

        public RegisterRequest(String account_number, String cif_number, String branch_code, String country, String mobile_number, String password, String transaction_pin) {
            this.account_number = account_number;
            this.cif_number = cif_number;
            this.branch_code = branch_code;
            this.country = country;
            this.mobile_number = mobile_number;
            this.password = password;
            this.transaction_pin = transaction_pin;
        }
    }

    @POST("netbankingapi/register.php")
    Call<LoginResponse> register(@Body RegisterRequest request);

    class Transaction {
        String type;
        double amount;
        String date;

        public String getType() { return type; }
        public double getAmount() { return amount; }
        public String getDate() { return date; }
    }

    class TransactionsResponse {
        boolean success;
        List<Transaction> transactions;

        public boolean isSuccess() { return success; }
        public List<Transaction> getTransactions() { return transactions; }
    }

    class TransactionsRequest {
        String cif_number;
        public TransactionsRequest(String cif_number) {
            this.cif_number = cif_number;
        }
    }

    @POST("netbankingapi/get_transactions.php")
    Call<TransactionsResponse> getTransactions(@Body TransactionsRequest request);

    class TransferRequest {
        String from_cif, to_cif, transaction_pin;
        double amount;

        public TransferRequest(String from_cif, String to_cif, double amount, String transaction_pin) {
            this.from_cif = from_cif;
            this.to_cif = to_cif;
            this.amount = amount;
            this.transaction_pin = transaction_pin;
        }
    }

    class TransferResponse {
        boolean success;
        String message;

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }

    @POST("netbankingapi/fund_transfer.php")
    Call<TransferResponse> transfer(@Body TransferRequest request);

    class ChangePasswordRequest {
        String cif_number, old_password, new_password;
        public ChangePasswordRequest(String cif_number, String old_password, String new_password) {
            this.cif_number = cif_number;
            this.old_password = old_password;
            this.new_password = new_password;
        }
    }

    class ChangePinRequest {
        String cif_number, old_tpin, new_tpin;
        public ChangePinRequest(String cif_number, String old_tpin, String new_tpin) {
            this.cif_number = cif_number;
            this.old_tpin = old_tpin;
            this.new_tpin = new_tpin;
        }
    }

    class SimpleResponse {
        boolean success;
        String message;
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }

    @POST("netbankingapi/change_password.php")
    Call<SimpleResponse> changePassword(@Body ChangePasswordRequest req);

    @POST("netbankingapi/change_tpin.php")
    Call<SimpleResponse> changeTpin(@Body ChangePinRequest req);


    // Request
    class ProfileRequest {
        private String cif_number;
        public ProfileRequest(String cif_number) {
            this.cif_number = cif_number;
        }
    }

    // Response
    class ProfileResponse {
        private boolean success;
        private String account_number, cif_number, branch_code, country, mobile_number;

        public boolean isSuccess() { return success; }
        public String getAccount_number() { return account_number; }
        public String getCif_number() { return cif_number; }
        public String getBranch_code() { return branch_code; }
        public String getCountry() { return country; }
        public String getMobile() { return mobile_number; }
    }

    @POST("netbankingapi/get_profile.php")
    Call<ProfileResponse> getProfile(@Body ProfileRequest req);

    // Request
    class MiniStatementRequest {
        String cif_number;
        public MiniStatementRequest(String cif_number) { this.cif_number = cif_number; }
    }

    // Transaction item model (for Retrofit/Gson)
    class TransactionItem {
        int id;
        public String type;
        public double amount;
        public String description;
        public String date;
        // getters if needed
    }

    // Response
    class MiniStatementResponse {
        boolean success;
        String message;
        List<TransactionItem> transactions;
        public boolean isSuccess(){ return success; }
        public List<TransactionItem> getTransactions(){ return transactions; }
    }

    // Add to interface
    @POST("netbankingapi/get_mini_statement.php")
    Call<MiniStatementResponse> getMiniStatement(@Body MiniStatementRequest req);

    // Transaction Insights
    class InsightsRequest {
        String cif_number;
        public InsightsRequest(String cif_number) {
            this.cif_number = cif_number;
        }
    }

    class CategoryInsight {
        public String category;
        public double total_amount;
        int transaction_count;
    }

    class MonthlyTrend {
        public String month;
        public double total;
    }

    class InsightsResponse {
        boolean success;
        String message;
        List<CategoryInsight> insights;
        double total_expense;
        List<MonthlyTrend> monthly_trend;

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public List<CategoryInsight> getInsights() { return insights; }
        public double getTotal_expense() { return total_expense; }
        public List<MonthlyTrend> getMonthly_trend() { return monthly_trend; }
    }

    @POST("netbankingapi/get_transaction_insights.php")
    Call<InsightsResponse> getTransactionInsights(@Body InsightsRequest request);

}
