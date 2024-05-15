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
    public String receiver_id;
    public String receiver_avatar;
    public String receiver_full_name;
    public String last_message;
    public String last_message_time;
    public boolean has_message;
    public boolean is_online;
    public boolean is_new_chat;
    @Ignore
    public List<Message> message_list;
}
