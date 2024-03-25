package com.tech.cybercars.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chat")
public class Chat {
    @PrimaryKey
    @NonNull
    public String id;
    public String sender_id;
    public String sender_avatar;
    public String sender_full_name;

    public Chat(@NonNull String id, String sender_id, String sender_avatar, String sender_full_name) {
        this.id = id;
        this.sender_id = sender_id;
        this.sender_avatar = sender_avatar;
        this.sender_full_name = sender_full_name;
    }
}
