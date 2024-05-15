package com.tech.cybercars.services.eventbus;

import com.tech.cybercars.data.models.chat.Chat;

public class ChatEvent {
    public final static String NEW_CHAT = "NEW_CHAT";
    public String action;
    public Chat chat;

    public ChatEvent(String action) {
        this.action = action;
    }
}
