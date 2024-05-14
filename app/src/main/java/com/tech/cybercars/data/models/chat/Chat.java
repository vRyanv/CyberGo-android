package com.tech.cybercars.data.models.chat;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "chat")
public class Chat implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String chat_id;
    public String user_receive_id;
    public String user_receive_avatar;
    public String user_receive_full_name;
    public String last_message;
    public String last_message_time;
    public boolean has_message;
    public boolean is_online;
    @Ignore
    public List<Message> message_list;
    public Chat(){}

    public Chat(String chat_id, String user_receive_id, String user_receive_avatar, String user_receive_full_name, String last_message, String last_message_time, boolean has_message, boolean is_online) {
        this.chat_id = chat_id;
        this.user_receive_id = user_receive_id;
        this.user_receive_avatar = user_receive_avatar;
        this.user_receive_full_name = user_receive_full_name;
        this.last_message = last_message;
        this.last_message_time = last_message_time;
        this.has_message = has_message;
        this.is_online = is_online;
    }
}
