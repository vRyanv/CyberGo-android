package com.tech.cybercars.services.eventbus;

public class NotificationEvent {
    public String title;
    public String content;
    public int data_id;

    public NotificationEvent(String title, String content, int data_id) {
        this.title = title;
        this.content = content;
        this.data_id = data_id;
    }
}
