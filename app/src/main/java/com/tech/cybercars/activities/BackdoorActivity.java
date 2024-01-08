package com.tech.cybercars.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.tech.cybercars.R;
import com.tech.cybercars.ui.main.MainActivity;
import com.tech.cybercars.ui.splash.SplashActivity;

public class BackdoorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backdoor);
        findViewById(R.id.btn_go_splash_bd).setOnClickListener(view -> {
            startActivity(new Intent(this, SplashActivity.class));
        });

        findViewById(R.id.btn_go_home_bd).setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
        });
    }
}