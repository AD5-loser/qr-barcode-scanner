package com.example.qrcodeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {

    private EditText textInput;
    private ImageView qrImageView;
    private TextView scanResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI Elements
        textInput = findViewById(R.id.textInput);
        qrImageView = findViewById(R.id.qrImageView);
        scanResultView = findViewById(R.id.scanResultView);
        Button generateButton = findViewById(R.id.generateButton);
        Button scanButton = findViewById(R.id.scanButton);

        // Generate QR Code
        generateButton.setOnClickListener(v -> generateQRCode());

        // Scan QR Code
        scanButton.setOnClickListener(v -> startQRCodeScanner());
    }

    private void generateQRCode() {
        String text = textInput.getText().toString().trim();

        if (text.isEmpty()) {
            Toast.makeText(this, "টেক্সট এন্টার করুন!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);

            qrImageView.setImageBitmap(bitmap);
            scanResultView.setText("✅ QR Code তৈরি হয়েছে!");

        } catch (WriterException e) {
            Toast.makeText(this, "ত্রুটি: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void startQRCodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("QR Code টি ক্যামেরার সামনে রাখুন");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "স্ক্যান বাতিল করা হয়েছে", Toast.LENGTH_SHORT).show();
            } else {
                String scanText = result.getContents();
                scanResultView.setText("📝 ফলাফল: " + scanText);
                textInput.setText(scanText);
                Toast.makeText(this, "✅ স্ক্যান সম্পন্ন!", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
