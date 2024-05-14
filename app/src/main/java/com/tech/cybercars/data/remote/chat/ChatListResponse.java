package com.tech.cybercars.data.remote.chat;

import com.tech.cybercars.data.models.chat.Chat;
import com.tech.cybercars.data.remote.base.BaseResponse;

import java.util.List;

public class ChatListResponse extends BaseResponse {
    public List<Chat> chat_list;
}
