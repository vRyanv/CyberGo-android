package com.tech.cybercars.services.fcm;

import android.app.Dialog;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tech.cybercars.CyberGoApplication;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.data.local.AppDBContext;
import com.tech.cybercars.data.models.Notification;
import com.tech.cybercars.services.eventbus.ActionEvent;
import com.tech.cybercars.services.notification.NotificationService;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class CyberGoNotification extends FirebaseMessagingService {
    public static final String NOTIFICATION = "NOTIFICATION";
    public static final String CHAT = "CHAT";
    public static final String ACCOUNT_BANNED = "ACCOUNT_BANNED";
    public static final String FIREBASE_TYPE = "firebase_type";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Map<String, String> message_data = message.getData();
        String type = message_data.get(FIREBASE_TYPE);

        assert type != null;
        if(type.equals(NOTIFICATION)){
            ReceiveNotification(message_data);
        } else if(type.equals(CHAT)){

        }else if(type.equals(ACCOUNT_BANNED)){
            AccountBanned();
        }

    }

    private void AccountBanned(){
        Log.d(Tag.CYBER_DEBUG, "AccountBanned");
        EventBus.getDefault().post(new ActionEvent(ActionEvent.SHOW_ACCOUNT_BANNED_DIALOG));
    }

    private void ReceiveNotification(Map<String, String> notification_data){
        String id = notification_data.get(FieldName._ID);
        String datetime = notification_data.get(FieldName.DATETIME);
        String avatar = notification_data.get(FieldName.AVATAR);
        String title = notification_data.get(FieldName.TITLE);
        String content = notification_data.get(FieldName.CONTENT);


        assert datetime != null;
        assert id != null;
        Notification notification = new Notification(id, title, avatar, Long.parseLong(datetime), content);
        NotificationService.PushNormal(getApplicationContext(), notification.avatar, notification.title, notification.content);
        EventBus.getDefault().post(notification);
        AppDBContext.GetInstance(this).NotificationDAO().InsertNotification(notification);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}
