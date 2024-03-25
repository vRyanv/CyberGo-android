package com.tech.cybercars.data.remote.notification;

import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.remote.base.BaseResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface NotificationServiceRetrofit {
    @GET(URL.NOTIFICATIONS)
    Call<NotificationResponse> GetNotifications(
            @Header("authorization") String user_token
    );
}
