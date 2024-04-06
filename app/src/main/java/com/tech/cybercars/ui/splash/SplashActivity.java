package com.tech.cybercars.ui.splash;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.ui.welcome.WelcomeActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.rotate_core);
        animator.setTarget(findViewById(R.id.img_logo_plash_screen_core));
        animator.start();

        animator = AnimatorInflater.loadAnimator(this, R.animator.logo_translate);
        animator.setTarget(findViewById(R.id.container_logo_splash));
        animator.start();

        new Handler().postDelayed(()->{startActivity(new Intent(this, WelcomeActivity.class));}, DelayTime.SPLASH_SCREEN);
    }
}