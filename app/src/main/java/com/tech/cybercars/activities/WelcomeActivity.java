package com.tech.cybercars.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

import com.tech.cybercars.R;
import com.tech.cybercars.ui.signup.SignUpActivity;

public class WelcomeActivity extends AppCompatActivity {
    private AppCompatButton btn_create_account, btn_go_to_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btn_go_to_login = findViewById(R.id.btn_go_to_login);
        btn_create_account = findViewById(R.id.btn_create_account);
        btn_create_account.setOnClickListener(view -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });

        btn_go_to_login.setOnClickListener(view -> {
            startActivity(new Intent(this, test.class));
        });
    }

}