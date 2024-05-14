package com.tech.cybercars.data.local.chat;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tech.cybercars.data.models.chat.Chat;

import java.util.List;

@Dao
public interface ChatDAO {
    @Insert
    void InsertChat(List<Chat> chats);

    @Insert
    void InsertChat(Chat chats);

    @Query("SELECT * FROM chat")
    List<Chat> GetAllChat();

    @Query("DELETE FROM chat")
    List<Chat> ClearTable();
}
