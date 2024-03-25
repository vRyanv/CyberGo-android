package com.tech.cybercars.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tech.cybercars.data.local.notification.NotificationDAO;
import com.tech.cybercars.data.models.Chat;
import com.tech.cybercars.data.models.Message;
import com.tech.cybercars.data.models.Notification;
import com.tech.cybercars.data.models.User;
import com.tech.cybercars.data.local.user.UserDAO;

@Database(entities = {
        User.class,
        Notification.class,
        Chat.class,
        Message.class
}, version = 1)
public abstract class AppDBContext extends RoomDatabase {
    private static final String DATABASE_NAME = "cyber_go.db";
    private static AppDBContext instance;

    public static synchronized AppDBContext GetInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDBContext.class,
                            DATABASE_NAME
                    ).allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract UserDAO UserDao();
    public abstract NotificationDAO NotificationDAO();
}
