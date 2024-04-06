package com.tech.cybercars.services.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.ui.main.notification.NotificationActivity;

import java.util.Date;

public class NotificationService {
    public static final String NOTIFY_NORMAL = "NOTIFY_NORMAL";
    public static final String NOTIFY_MESSAGE = "NOTIFY_MESSAGE";

    public static void PushNormal(Context context, String avatar, String title, String content) {
        Uri audio_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + avatar;
        Glide.with(context)
                .asBitmap()
                .load(avatar_full_path)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Notification notification = new NotificationCompat.Builder(context, Tag.NORMAL_CHANEL_ID)
                                .setLargeIcon(resource)
                                .setSmallIcon(R.drawable.ic_success)
                                .setContentTitle(title)
                                .setContentText(content)
                                .setSound(audio_uri)
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .setAutoCancel(true).build();
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        if(notificationManager != null){
                            notificationManager.notify(GenerateNotificationId(), notification);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public static void PushMessage(Context context, String title, String content, String user_id) {
        Intent resultIntent = new Intent(context, NotificationActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

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
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true).build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(GenerateNotificationId(), notification);
        }
    }

    private static int GenerateNotificationId() {
        return (int) new Date().getTime();
    }
}
