package com.example.chiplanner; 

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Durasi splash screen (3000ms = 3 detik)
        int durasi = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Setelah 3 detik, pindah ke LoginActivity
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Agar user tidak bisa kembali ke splash screen saat tekan tombol back
            }
        }, durasi);
    }
}