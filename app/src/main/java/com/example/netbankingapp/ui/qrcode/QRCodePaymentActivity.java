package com.example.netbankingapp.ui.qrcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.netbankingapp.R;
import com.example.netbankingapp.network.ApiClient;
import com.example.netbankingapp.network.ApiService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;

public class QRCodePaymentActivity extends AppCompatActivity {

    private static final String TAG = "QRCodePayment";
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    
    private ImageView ivQRCode;
    private TextView tvQRInfo;
    private Button btnGenerateQR, btnScanQR, btnProceedTransfer;
    private EditText etAmount, etTransactionPin;
    private View cardQRDisplay, cardTransferForm;
    
    private String senderCif, senderAccountNumber;
    private String scannedCif, scannedAccountNumber;
    private String qrData;
    
    private ActivityResultLauncher<ScanOptions> qrCodeLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qr_code_payment);

        initViews();
        
        senderCif = getIntent().getStringExtra("cif_number");
        if (senderCif == null) {
            Toast.makeText(this, "CIF number not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch profile to get account number
        fetchProfile();

        // Initialize QR code scanner launcher
        qrCodeLauncher = registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show();
            } else {
                String scannedContent = result.getContents();
                handleScannedQRCode(scannedContent);
            }
        });

        btnGenerateQR.setOnClickListener(v -> generateQRCode());
        btnScanQR.setOnClickListener(v -> scanQRCode());
        btnProceedTransfer.setOnClickListener(v -> proceedToTransfer());
    }

    private void initViews() {
        ivQRCode = findViewById(R.id.ivQRCode);
        tvQRInfo = findViewById(R.id.tvQRInfo);
        btnGenerateQR = findViewById(R.id.btnGenerateQR);
        btnScanQR = findViewById(R.id.btnScanQR);
        btnProceedTransfer = findViewById(R.id.btnProceedTransfer);
        etAmount = findViewById(R.id.etAmount);
        etTransactionPin = findViewById(R.id.etTransactionPin);
        cardQRDisplay = findViewById(R.id.cardQRDisplay);
        cardTransferForm = findViewById(R.id.cardTransferForm);
        
        // Initially hide cards
        cardQRDisplay.setVisibility(View.GONE);
        cardTransferForm.setVisibility(View.GONE);
    }

    private void fetchProfile() {
        Log.d(TAG, "Fetching profile for CIF: " + senderCif);
        ApiService.ProfileRequest req = new ApiService.ProfileRequest(senderCif);
        ApiClient.getApiService().getProfile(req).enqueue(new Callback<ApiService.ProfileResponse>() {
            @Override
            public void onResponse(Call<ApiService.ProfileResponse> call, Response<ApiService.ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    senderAccountNumber = response.body().getAccount_number();
                    Log.d(TAG, "Profile loaded. Account number: " + senderAccountNumber);
                    // Enable generate QR button
                    btnGenerateQR.setEnabled(true);
                } else {
                    Log.e(TAG, "Failed to load profile. Response: " + response);
                    Toast.makeText(QRCodePaymentActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                    btnGenerateQR.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<ApiService.ProfileResponse> call, Throwable t) {
                Log.e(TAG, "Error fetching profile: " + t.getMessage(), t);
                Toast.makeText(QRCodePaymentActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                btnGenerateQR.setEnabled(false);
            }
        });
    }

    private void generateQRCode() {
        if (senderAccountNumber == null || senderAccountNumber.isEmpty()) {
            Toast.makeText(this, "Account number not available. Please wait...", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Account number is null or empty");
            return;
        }

        // QR code format: CIF|ACCOUNT_NUMBER
        qrData = senderCif + "|" + senderAccountNumber;
        Log.d(TAG, "Generating QR code for: " + qrData);
        
        try {
            // Use a smaller, more reasonable size for QR code
            Bitmap qrBitmap = generateQRCodeBitmap(qrData, 512, 512);
            if (qrBitmap != null && !qrBitmap.isRecycled()) {
                ivQRCode.setImageBitmap(qrBitmap);
                ivQRCode.setVisibility(View.VISIBLE);
                tvQRInfo.setText("Your QR Code\nCIF: " + senderCif + "\nAccount: " + senderAccountNumber);
                tvQRInfo.setVisibility(View.VISIBLE);
                cardQRDisplay.setVisibility(View.VISIBLE);
                cardTransferForm.setVisibility(View.GONE);
                Log.d(TAG, "QR code generated and displayed successfully");
            } else {
                Log.e(TAG, "QR bitmap is null or recycled");
                Toast.makeText(this, "Failed to generate QR code", Toast.LENGTH_SHORT).show();
            }
        } catch (WriterException e) {
            Log.e(TAG, "WriterException: " + e.getMessage(), e);
            e.printStackTrace();
            Toast.makeText(this, "Error generating QR code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "OutOfMemoryError: " + e.getMessage(), e);
            Toast.makeText(this, "Not enough memory to generate QR code", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error: " + e.getMessage(), e);
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap generateQRCodeBitmap(String data, int width, int height) throws WriterException {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 2);

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height, hints);

            int matrixWidth = bitMatrix.getWidth();
            int matrixHeight = bitMatrix.getHeight();
            
            Log.d(TAG, "BitMatrix dimensions: " + matrixWidth + "x" + matrixHeight);
            
            // Create pixel array
            int[] pixels = new int[matrixWidth * matrixHeight];
            for (int y = 0; y < matrixHeight; y++) {
                int offset = y * matrixWidth;
                for (int x = 0; x < matrixWidth; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }

            // Create bitmap with ARGB_8888 config for better compatibility
            Bitmap bitmap = Bitmap.createBitmap(matrixWidth, matrixHeight, Bitmap.Config.ARGB_8888);
            if (bitmap == null) {
                Log.e(TAG, "Failed to create bitmap");
                throw new WriterException("Failed to create bitmap");
            }
            
            bitmap.setPixels(pixels, 0, matrixWidth, 0, 0, matrixWidth, matrixHeight);
            Log.d(TAG, "Bitmap created successfully: " + bitmap.getWidth() + "x" + bitmap.getHeight());
            return bitmap;
        } catch (WriterException e) {
            Log.e(TAG, "WriterException in generateQRCodeBitmap: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            Log.e(TAG, "Exception in generateQRCodeBitmap: " + e.getMessage(), e);
            throw new WriterException("Failed to generate QR code: " + e.getMessage());
        }
    }

    private void scanQRCode() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.CAMERA}, 
                    CAMERA_PERMISSION_REQUEST_CODE);
            return;
        }
        launchQRScanner();
    }

    private void launchQRScanner() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan QR Code");
        options.setCameraId(0);
        options.setBeepEnabled(true);
        options.setBarcodeImageEnabled(true);
        qrCodeLauncher.launch(options);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchQRScanner();
            } else {
                Toast.makeText(this, "Camera permission is required to scan QR codes", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleScannedQRCode(String scannedData) {
        // Parse QR code data: CIF|ACCOUNT_NUMBER
        String[] parts = scannedData.split("\\|");
        if (parts.length != 2) {
            Toast.makeText(this, "Invalid QR code format", Toast.LENGTH_SHORT).show();
            return;
        }

        scannedCif = parts[0].trim();
        scannedAccountNumber = parts[1].trim();

        // Check if scanning own QR code
        if (scannedCif.equals(senderCif)) {
            Toast.makeText(this, "Cannot transfer to your own account", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch receiver account details
        fetchReceiverAccount(scannedCif);
    }

    private void fetchReceiverAccount(String cif) {
        ApiService.ProfileRequest req = new ApiService.ProfileRequest(cif);
        ApiClient.getApiService().getProfile(req).enqueue(new Callback<ApiService.ProfileResponse>() {
            @Override
            public void onResponse(Call<ApiService.ProfileResponse> call, Response<ApiService.ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    scannedAccountNumber = response.body().getAccount_number();
                    // Show transfer form
                    cardQRDisplay.setVisibility(View.VISIBLE);
                    tvQRInfo.setText("Receiver Details\nCIF: " + scannedCif + "\nAccount: " + scannedAccountNumber);
                    ivQRCode.setVisibility(View.GONE);
                    cardTransferForm.setVisibility(View.VISIBLE);
                    etAmount.setText("");
                    etTransactionPin.setText("");
                } else {
                    Toast.makeText(QRCodePaymentActivity.this, "Receiver account not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiService.ProfileResponse> call, Throwable t) {
                Toast.makeText(QRCodePaymentActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void proceedToTransfer() {
        String amountStr = etAmount.getText().toString().trim();
        String tpin = etTransactionPin.getText().toString().trim();

        if (amountStr.isEmpty() || tpin.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (scannedCif == null || scannedCif.isEmpty()) {
            Toast.makeText(this, "No receiver selected", Toast.LENGTH_SHORT).show();
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

        // Proceed to fund transfer
        ApiService.TransferRequest req = new ApiService.TransferRequest(senderCif, scannedCif, amount, tpin);
        ApiClient.getApiService().transfer(req).enqueue(new Callback<ApiService.TransferResponse>() {
            @Override
            public void onResponse(Call<ApiService.TransferResponse> call, Response<ApiService.TransferResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(QRCodePaymentActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if (response.body().isSuccess()) {
                        finish(); // Return to dashboard
                    }
                } else {
                    Toast.makeText(QRCodePaymentActivity.this, "Transfer failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiService.TransferResponse> call, Throwable t) {
                Toast.makeText(QRCodePaymentActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

