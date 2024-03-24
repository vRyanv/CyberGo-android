package com.tech.cybercars.services.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.Tag;

import java.util.Date;

public class NotificationService {
    public static final String NOTIFY_NORMAL = "NOTIFY_NORMAL";
    public static final String NOTIFY_MESSAGE = "NOTIFY_MESSAGE";
    public static void PushNormal(Context context, String title, String content){
        Uri audio_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification);
        Notification notification = new NotificationCompat.Builder(context, Tag.NORMAL_CHANEL_ID)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.ic_success)
                .setContentTitle(title)
                .setContentText(content)
                .setSound(audio_uri)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setPriority(NotificationCompat.PRIORITY_MAX).build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null){
            notificationManager.notify(GenerateNotificationId(), notification);
        }
    }

    public static void PushMessage(Context context, String title, String content){
        Uri audio_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification);
        Notification notification = new NotificationCompat.Builder(context, Tag.MESSAGE_CHANEL_ID)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.ic_success)
                .setContentTitle(title)
                .setContentText(content)
                .setSound(audio_uri)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setPriority(NotificationCompat.PRIORITY_MAX).build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null){
            notificationManager.notify(GenerateNotificationId(), notification);
        }
    }

    private static int GenerateNotificationId(){
        return (int) new Date().getTime();
    }
}
