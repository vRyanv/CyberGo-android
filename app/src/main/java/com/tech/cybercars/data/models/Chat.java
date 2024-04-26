package com.tech.cybercars.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "chat")
public class Chat implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String chat_id;
    public String sender_id;
    public String sender_avatar;
    public String sender_full_name;
    public String last_message;
    public String last_message_time;
    public boolean has_message;
    public boolean is_online;

    public Chat(String chat_id, String sender_id, String sender_avatar, String sender_full_name, String last_message, String last_message_time, boolean has_message, boolean is_online) {
        this.chat_id = chat_id;
        this.sender_id = sender_id;
        this.sender_avatar = sender_avatar;
        this.sender_full_name = sender_full_name;
        this.last_message = last_message;
        this.last_message_time = last_message_time;
        this.has_message = has_message;
        this.is_online = is_online;
    }
}
