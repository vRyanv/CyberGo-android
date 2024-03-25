package com.tech.cybercars;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.tech.cybercars.constant.Tag;

public class CyberGoApplication extends Application {
    public boolean is_background_state;
    public static CyberGoApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        CreateNormalChanelNotification();
        CreateMessageNotification();
        ActivityLifecycle();
    }


    private void ActivityLifecycle() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {}

            @Override
            public void onActivityStarted(@NonNull Activity activity) {}

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                is_background_state = true;
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                is_background_state = false;
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {}

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {}

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {}
        });
    }

    private void CreateNormalChanelNotification(){
        NotificationChannel normal_channel = new NotificationChannel(
                Tag.NORMAL_CHANEL_ID,
                Tag.NORMAL_CHANEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT
        );
        Uri sound_uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification);
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        normal_channel.setSound(sound_uri, attributes);
        NotificationManager notification_manager = getSystemService(NotificationManager.class);
        notification_manager.createNotificationChannel(normal_channel);
    }


    private void CreateMessageNotification() {
        NotificationChannel message_channel = new NotificationChannel(
                Tag.MESSAGE_CHANEL_ID,
                Tag.MESSAGE_CHANEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT
        );
        Uri sound_uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.message);
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        message_channel.setSound(sound_uri, attributes);
        NotificationManager notification_manager = getSystemService(NotificationManager.class);
        notification_manager.createNotificationChannel(message_channel);
    }
}
