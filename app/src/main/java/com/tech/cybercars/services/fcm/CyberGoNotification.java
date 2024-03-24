package com.tech.cybercars.services.fcm;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tech.cybercars.CyberGoApplication;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.Map;

import timber.log.Timber;

public class CyberGoNotification extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Map<String, String> notification_data = message.getData();
        String type_notification = notification_data.get(FieldName.TYPE);
        String score = notification_data.get("score");
        Log.e(Tag.CYBER_DEBUG, score);
        if(CyberGoApplication.instance.is_background_state){
            String title = notification_data.get(FieldName.TITLE);
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}
