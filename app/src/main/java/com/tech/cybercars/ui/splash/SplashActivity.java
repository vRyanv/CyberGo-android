package com.tech.cybercars.ui.splash;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.ui.main.MainActivity;
import com.tech.cybercars.ui.welcome.WelcomeActivity;
import com.tech.cybercars.utils.SharedPreferencesUtil;

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

        new Handler().postDelayed(
                () -> {
                    String user_token = SharedPreferencesUtil.GetString(this, SharedPreferencesUtil.USER_TOKEN_KEY);
                    if(user_token.equals("")){
                        startActivity(new Intent(this, WelcomeActivity.class));
                        return;
                    }
                    Intent home_intent = new Intent(this, MainActivity.class);
                    home_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(home_intent);
                    finish();
                },
                DelayTime.SPLASH_SCREEN
        );
    }
}