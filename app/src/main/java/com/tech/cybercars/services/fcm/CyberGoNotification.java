package com.tech.cybercars.services.fcm;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.data.local.AppDBContext;
import com.tech.cybercars.data.models.Notification;
import com.tech.cybercars.services.notification.NotificationService;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class CyberGoNotification extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Map<String, String> notification_data = message.getData();
        String id = notification_data.get(FieldName._ID);
        String datetime = notification_data.get(FieldName.DATETIME);
        String avatar = notification_data.get(FieldName.AVATAR);
        String title = notification_data.get(FieldName.TITLE);
        String content = notification_data.get(FieldName.CONTENT);
        String type = notification_data.get(FieldName.TYPE);

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
