package com.tech.cybercars.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "message")
public class Message {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String message_id;
    public String chat_id;
    public String sender_id;
    public String avatar;
    public String content;
    public String time;

    public Message(String message_id, String chat_id, String sender_id, String avatar, String content, String time) {
        this.message_id = message_id;
        this.chat_id = chat_id;
        this.sender_id = sender_id;
        this.avatar = avatar;
        this.content = content;
        this.time = time;
    }
}
