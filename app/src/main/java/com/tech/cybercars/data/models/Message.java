package com.tech.cybercars.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "message")
public class Message {
    @PrimaryKey
    @NonNull
    public String id;
    public String chat_id;
    public String sender_id;
    public String avatar;
    public String content;
    public Long datetime;

    public Message(@NonNull String id, String chat_id, String sender_id, String avatar, String content, Long datetime) {
        this.id = id;
        this.chat_id = chat_id;
        this.sender_id = sender_id;
        this.avatar = avatar;
        this.content = content;
        this.datetime = datetime;
    }
}
