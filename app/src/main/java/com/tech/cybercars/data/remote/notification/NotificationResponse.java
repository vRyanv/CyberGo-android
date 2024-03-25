package com.tech.cybercars.data.remote.notification;

import com.tech.cybercars.data.models.Notification;
import com.tech.cybercars.data.remote.base.BaseResponse;

import java.util.List;

public class NotificationResponse extends BaseResponse {
    public List<Notification> notification_list;
}
