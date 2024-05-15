package com.tech.cybercars.data.models.chat;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "message")
public class Message {
    @PrimaryKey
    @NonNull
    public String message_id;
    public String chat_id;
    public String sender_id;
    public String sender_avatar;
    public String content;
    public String send_time;
}
