package com.tech.cybercars.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tech.cybercars.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.rotate_core);
        animator.setTarget(findViewById(R.id.img_logo_plash_screen_core));
        animator.start();

        animator = AnimatorInflater.loadAnimator(this, R.animator.logo_translate);
        animator.setTarget(findViewById(R.id.container_logo_splash));
        animator.start();

        new Handler().postDelayed(()->{startActivity(new Intent(this, WelcomeActivity.class));}, 2000);
    }
}