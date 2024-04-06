package com.tech.cybercars.data.repositories;

import com.tech.cybercars.data.remote.notification.NotificationResponse;
import com.tech.cybercars.data.remote.notification.NotificationServiceRetrofit;
import com.tech.cybercars.data.remote.retrofit.ResFailCallback;
import com.tech.cybercars.data.remote.retrofit.ResSuccessCallback;
import com.tech.cybercars.data.remote.retrofit.RetrofitRequest;
import com.tech.cybercars.data.remote.retrofit.RetrofitResponse;

public class NotificationRepository {
    private NotificationServiceRetrofit notification_service;
    private static NotificationRepository notification_repository;
    public static NotificationRepository GetInstance(){
        if(notification_repository == null){
            notification_repository = new NotificationRepository();
        }
        return notification_repository;
    }

    private NotificationRepository() {
        notification_service = RetrofitRequest.getInstance().create(NotificationServiceRetrofit.class);
    }

    public void GetNotificationList(String user_token, ResSuccessCallback<NotificationResponse> success_callback, ResFailCallback fail_callback){
        notification_service.GetNotifications(user_token)
                .enqueue(new RetrofitResponse<NotificationResponse>().GetResponse(success_callback, fail_callback));
    }
}
