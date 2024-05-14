package com.tech.cybercars.data.remote.chat;

import com.tech.cybercars.data.models.chat.Chat;
import com.tech.cybercars.data.remote.base.BaseResponse;

public class PrivateChatResponse extends BaseResponse {
    public boolean is_new_chat;
    public Chat chat;
}
