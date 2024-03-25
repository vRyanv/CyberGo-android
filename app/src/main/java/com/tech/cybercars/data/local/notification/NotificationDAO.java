package com.tech.cybercars.data.local.notification;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tech.cybercars.data.models.Notification;
import com.tech.cybercars.data.models.User;

import java.util.List;

@Dao
public interface NotificationDAO {
    @Insert
    void InsertNotification(Notification notification);

    @Query("SELECT * FROM notification")
    List<Notification> GetNotificationList();
}
