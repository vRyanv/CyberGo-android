package com.tech.cybercars.data.local.notification;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tech.cybercars.data.models.Notification;

import java.util.List;

@Dao
public interface NotificationDAO {
    @Insert
    void InsertNotification(Notification notification);
    @Insert
    void InsertNotification(List<Notification> notifications);

    @Query("SELECT * FROM notification ORDER BY datetime DESC")
    List<Notification> GetNotificationList();

    @Query("DELETE FROM notification")
    public void ClearTable();
}
