package com.tech.cybercars.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification")
public class Notification{
    @PrimaryKey
    @NonNull
    public String id;
    //datetime as timestamp
    public Long datetime;
    public String avatar;
    public String title;
    public String content;

    public Notification(@NonNull String id, String title, String avatar, Long datetime, String content) {
        this.id = id;
        this.title = title;
        this.avatar = avatar;
        this.datetime = datetime;
        this.content = content;
    }
}
