package com.tech.cybercars.data.local.chat;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tech.cybercars.data.models.chat.Message;

import java.util.List;

@Dao
public interface MessageDAO {
    @Query("SELECT * FROM message WHERE chat_id = :chat_id")
    public List<Message> GetMessageList(String chat_id);

    @Query("SELECT * FROM message WHERE chat_id = (SELECT chat_id FROM chat WHERE user_receive_id = :user_receive_id)")
    public List<Message> GetMessageListByReceiveId(String user_receive_id);

    @Insert
    public void InsertMessage(Message message);

    @Insert
    public void InsertMessage(List<Message> message);

    @Query("DELETE FROM message")
    public void ClearTable();
}
