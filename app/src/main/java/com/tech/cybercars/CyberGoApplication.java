package com.tech.cybercars;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.data.local.AppDBContext;
import com.tech.cybercars.data.remote.user.fcm.UpdateFCMBody;
import com.tech.cybercars.data.repositories.UserRepository;
import com.tech.cybercars.services.eventbus.ActionEvent;
import com.tech.cybercars.services.socket.SocketService;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.ui.signin.SignInActivity;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyberGoApplication extends Application {
    public static CyberGoApplication instance;
    private Activity current_activity;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        EventBus.getDefault().register(this);
        Log.e(Tag.CYBER_DEBUG, "CyberGoApplication: onCreate");
        CreateNormalChanelNotification();
        CreateMessageNotification();
        ActivityLifecycle();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnAccountBannedEvent(ActionEvent action_event){
        if(action_event.action.equals(ActionEvent.SHOW_ACCOUNT_BANNED_DIALOG)){
            if(current_activity == null){
                CyberGoApplication.instance.Logout();
                return;
            }
            NotificationDialog.Builder(current_activity)
                    .SetIcon(R.drawable.ic_warning)
                    .SetTitle(getResources().getString(R.string.locked))
                    .SetSubtitle(getResources().getString(R.string.your_account_has_been_locked))
                    .SetTextMainButton(getResources().getString(R.string.logout))
                    .SetOnMainButtonClicked(dialog-> {
                        CyberGoApplication.instance.Logout();
                    }).show();
        }
    }


    private void ActivityLifecycle() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
                current_activity = activity;
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                StartSocketService();
                current_activity = activity;
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                Log.e(Tag.CYBER_DEBUG, "CyberGoApplication: onActivityResumed");
                current_activity = activity;
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                Log.e(Tag.CYBER_DEBUG, "CyberGoApplication: onActivityPaused");
                if (current_activity == activity) {
                    current_activity = null;
                }
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                Log.e(Tag.CYBER_DEBUG, "CyberGoApplication: onActivityStopped");
                if (current_activity == activity) {
                    current_activity = null;
                }
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {}

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                Log.e(Tag.CYBER_DEBUG, "CyberGoApplication: onActivityDestroyed");
                if (current_activity == activity) {
                    current_activity = null;
                }
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

    public void Logout(){
        RemoveFirebaseToken();
        ClearApplicationData();
    }

    private void RemoveFirebaseToken(){
        UpdateFCMBody update_firebase_token_body = new UpdateFCMBody();
        update_firebase_token_body.firebase_token = "";
        String user_token = SharedPreferencesUtil.GetString(this, SharedPreferencesUtil.USER_TOKEN_KEY);
        UserRepository.GetInstance().UpdateFirebaseToken(
                user_token,
                update_firebase_token_body,
                response -> {
                    if (!response.isSuccessful() || response.body() == null) {
                        Toast.makeText(this, "Update firebase token fail", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (response.body().code != StatusCode.UPDATED) {
                        Toast.makeText(this, response.body().message, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(Tag.CYBER_DEBUG, "RemoveFireBaseTokenOnServer: " + error.getMessage() );
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );
    }

    private void ClearApplicationData(){
        ExecutorService executor_service = Executors.newSingleThreadExecutor();
        executor_service.execute(() -> {
            EventBus.getDefault().post(new ActionEvent(ActionEvent.STOP_SOCKET));
            AppDBContext app_context_db = AppDBContext.GetInstance(this);
            app_context_db.VehicleDAO().ClearTable();
            app_context_db.UserDao().ClearTable();
            app_context_db.TripDAO().ClearTable();
            app_context_db.MemberDAO().ClearTable();
            app_context_db.DestinationDAO().ClearTable();
            app_context_db.NotificationDAO().ClearTable();
            app_context_db.ChatDAO().ClearTable();

            SharedPreferencesUtil.Clear(this.getApplicationContext());

            Handler main_handler = new Handler(Looper.getMainLooper());
            main_handler.post(() -> {
                Intent sign_in_activity = new Intent(this.getApplicationContext(), SignInActivity.class);
                sign_in_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                this.getApplicationContext().startActivity(sign_in_activity);
            });
        });
        executor_service.shutdown();
    }
}
