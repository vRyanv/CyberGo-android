package com.tech.cybercars;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.services.socket.SocketService;

public class CyberGoApplication extends Application {
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
            public void onActivityStarted(@NonNull Activity activity) {
                StartSocketService();
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                Log.e(Tag.CYBER_DEBUG, "onActivityResumed");
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                Log.e(Tag.CYBER_DEBUG, "onActivityPaused");
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                Log.e(Tag.CYBER_DEBUG, "onActivityStopped");
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {}

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                Log.e(Tag.CYBER_DEBUG, "onActivityDestroyed");
            }
        });
    }

    public void StartSocketService(){
        if(!SocketService.is_running){
            Intent socket_intent = new Intent(this, SocketService.class);
            startService(socket_intent);
        }
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
                NotificationManager.IMPORTANCE_HIGH
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
