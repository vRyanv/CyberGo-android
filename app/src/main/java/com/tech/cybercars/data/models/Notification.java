package com.tech.cybercars.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification")
public class Notification{
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String notification_id;
    //datetime as timestamp
    public Long datetime;
    public String avatar;
    public String title;
    public String content;

    public Notification(String notification_id, String title, String avatar, Long datetime, String content) {
        this.notification_id = notification_id;
        this.title = title;
        this.avatar = avatar;
        this.datetime = datetime;
        this.content = content;
    }
}
